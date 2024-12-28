package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.categories.CategoriesApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Random;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
    private final CategoriesApiClient categoriesApiClient = new CategoriesApiClient();
    private final Random random = new Random();


    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
                .ifPresent(anno -> {
                    CategoryJson category = new CategoryJson(
                            null,
                            "Category_" + random.nextInt(1000),
                            anno.username(),
                            false);
                    CategoryJson createdCategory = categoriesApiClient.createCategory(category);

                    if (anno.archived()) {
                        CategoryJson categoryToBeArchived = new CategoryJson(
                                createdCategory.id(),
                                createdCategory.name(),
                                createdCategory.username(),
                                true
                        );
                        categoriesApiClient.updateCategory(categoryToBeArchived);
                    }
                    context.getStore(NAMESPACE).put(context.getUniqueId(), createdCategory);
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        CategoryJson categoryFromStore =
                context.getStore(CategoryExtension.NAMESPACE).get(context.getUniqueId(), CategoryJson.class);

        CategoryJson categoryToBeArchived = new CategoryJson(
                categoryFromStore.id(),
                categoryFromStore.name(),
                categoryFromStore.username(),
                true
        );
        categoriesApiClient.updateCategory(categoryToBeArchived);
    }
}