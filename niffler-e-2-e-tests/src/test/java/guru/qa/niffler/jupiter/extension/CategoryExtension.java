package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements
        BeforeEachCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(anno -> {
                    Category[] categories = anno.categories();

                    if (categories.length > 0) {
                        CategoryJson category = new CategoryJson(
                                null,
                                RandomDataUtils.randomCategoryName(),
                                anno.username(),
                                categories[0].archived()
                        );

                        CategoryJson created = spendApiClient.createCategory(category);
                        if (categories[0].archived()) {
                            CategoryJson archivedCategory = new CategoryJson(
                                    created.id(),
                                    created.name(),
                                    created.username(),
                                    true
                            );
                            created = spendApiClient.updateCategory(archivedCategory);
                        }

                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                created
                        );
                    }

                });
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
         CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (category != null) {
            if (!category.archived()) {
                category = new CategoryJson(
                        category.id(),
                        category.name(),
                        category.username(),
                        true
                );
                spendApiClient.updateCategory(category);
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }
}
