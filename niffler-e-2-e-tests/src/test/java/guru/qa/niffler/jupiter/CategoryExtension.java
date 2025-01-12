package guru.qa.niffler.jupiter;


import guru.qa.niffler.api.CategoryApiClient;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import static guru.qa.niffler.util.GenerateUtils.generateRandomCategory;
import static guru.qa.niffler.util.GenerateUtils.generateUUID;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final CategoryApiClient categoryApiClient = new CategoryApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
                .ifPresent(
                        anno -> {
                            CategoryJson categoryJson = new CategoryJson(
                                    generateUUID(),
                                    generateRandomCategory(),
                                    anno.username(),
                                    false
                            );
                            CategoryJson createdCategory = categoryApiClient.createCategory(categoryJson);

                            if(anno.archived()) {
                                CategoryJson archivedCategory = new CategoryJson(
                                        createdCategory.id(),
                                        createdCategory.name(),
                                        createdCategory.username(),
                                        true
                                );
                                createdCategory = categoryApiClient.updateCategory(archivedCategory);
                            }

                            context.getStore(CategoryExtension.NAMESPACE).put(context.getUniqueId(), createdCategory);
                        }
                );

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(CategoryExtension.NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson category = context.getStore(CategoryExtension.NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (category.archived()) {
            CategoryJson archiveCategory = new CategoryJson(
                    category.id(),
                    category.name(),
                    category.username(),
                    true
            );
            categoryApiClient.updateCategory(archiveCategory);
        }
    }
}
