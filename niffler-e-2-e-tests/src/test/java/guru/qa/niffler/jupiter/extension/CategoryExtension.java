package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.categories.CategoriesApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import static guru.qa.niffler.utils.DataUtils.randomCategoryName;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
    private final CategoriesApiClient categoriesApiClient = new CategoriesApiClient();
    private final SpendDbClient spendDbClient = new SpendDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(anno -> {
                    if (anno.categories().length > 0) {
                        Category category = anno.categories()[0];
                        CategoryJson categoryJson = new CategoryJson(
                                null,
                                randomCategoryName(),
                                anno.username(),
                                false);
                        CategoryJson createdCategory = spendDbClient.createCategory(categoryJson);

                        if (category.archived()) {
                            CategoryJson categoryToBeArchived = new CategoryJson(
                                    createdCategory.id(),
                                    createdCategory.name(),
                                    createdCategory.username(),
                                    true
                            );
                            createdCategory = categoriesApiClient.updateCategory(categoryToBeArchived);
                        }
                        context.getStore(NAMESPACE).put(context.getUniqueId(), createdCategory);
                    }
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
        if (categoryFromStore != null) {
            CategoryJson categoryToBeArchived = new CategoryJson(
                    categoryFromStore.id(),
                    categoryFromStore.name(),
                    categoryFromStore.username(),
                    true
            );
            categoriesApiClient.updateCategory(categoryToBeArchived);
        }
    }
}