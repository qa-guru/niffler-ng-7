package guru.qa.niffler.helpers.jupiter.extension;

import guru.qa.niffler.dataBase.service.SpendDbClient;
import guru.qa.niffler.helpers.dataGeneration.RandomDataUtils;
import guru.qa.niffler.helpers.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;


public class CategoryExtension implements BeforeEachCallback, ParameterResolver, AfterEachCallback {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final SpendDbClient spendDbClient = new SpendDbClient();

    @Override
    public void afterEach(ExtensionContext context) throws Exception {

        CategoryJson categoryJson = context.getStore(CategoryExtension.NAMESPACE).get(context.getUniqueId(), CategoryJson.class);

        if (categoryJson != null) {
            if (!categoryJson.archived()) {
                CategoryJson archivedCategory = new CategoryJson(
                        categoryJson.id(),
                        categoryJson.name(),
                        categoryJson.username(),
                        true
                );
                spendDbClient.deleteCategory(archivedCategory);
            }
        }
    }


    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        User user = context.getRequiredTestMethod().getAnnotation(User.class);
        if (user.categories().length > 0) {
            var categoryName = RandomDataUtils.getCategoryName();

            var category = spendDbClient.findCategoryByUsernameAndCategoryName(user.username(), categoryName);
            System.out.println(spendDbClient.findCategoryByUsernameAndCategoryName(user.username(), categoryName));

            if (category.isPresent() && category.get().name().equals(categoryName)) {
                spendDbClient.deleteCategory(
                        new CategoryJson(
                                category.get().id(),
                                category.get().name(),
                                category.get().username(),
                                category.get().archived()
                        ));
            }
            CategoryJson categoryJson = new CategoryJson(
                    null,
                    categoryName,
                    user.username(),
                    user.categories()[0].archived()
            );
            CategoryJson createdCategory = spendDbClient.createCategory(categoryJson);
            context.getStore(CategoryExtension.NAMESPACE).put(context.getUniqueId(), createdCategory);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(CategoryExtension.NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }
}
