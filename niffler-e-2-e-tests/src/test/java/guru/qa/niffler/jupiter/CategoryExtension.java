package guru.qa.niffler.jupiter;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.util.DataHelper;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
                .ifPresent(anno -> {
                            CategoryJson category = new CategoryJson(
                                    null,
                                    DataHelper.randomCategory(),
                                    anno.username(),
                                    false
                            );
                            CategoryJson categoryAdded = spendApiClient.addCategories(category);

                            //если нужно архивную, то редактируем новую категорию
                            if (anno.archived()) {
                                CategoryJson archivedCategory = new CategoryJson(
                                        categoryAdded.id(),
                                        categoryAdded.name(),
                                        categoryAdded.username(),
                                        true
                                );
                                categoryAdded = spendApiClient.updateCategory(archivedCategory);
                            }

                            context.getStore(NAMESPACE).put(
                                    context.getUniqueId(),
                                    categoryAdded
                            );
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
        if (!category.archived()) {
            CategoryJson archiveCategory = new CategoryJson(
                    category.id(),
                    category.name(),
                    category.username(),
                    true
            );
            spendApiClient.updateCategory(archiveCategory);
        }
    }
}
