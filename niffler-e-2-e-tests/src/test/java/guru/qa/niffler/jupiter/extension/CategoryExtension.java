package guru.qa.niffler.jupiter.extension;


import guru.qa.niffler.api.category.CategoryApiClient;
import guru.qa.niffler.helper.UserDataHelper;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final CategoryApiClient CategoryApiClient = new CategoryApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                            if (userAnno.categories().length > 0) {
                                Category anno = userAnno.categories()[0];

                                CategoryJson Category = new CategoryJson(
                                        null,
                                        UserDataHelper.getRandomCategory(),
                                        userAnno.username(),
                                        false
                                );
                                CategoryJson createdCategory = CategoryApiClient.createCategory(Category);

                                if (anno.archived()) {
                                    CategoryJson archivedCategory = new CategoryJson(
                                            createdCategory.id(),
                                            createdCategory.name(),
                                            createdCategory.username(),
                                            true
                                    );
                                    createdCategory = CategoryApiClient.updateCategory(archivedCategory);
                                }

                                context.getStore(NAMESPACE).put(context.getUniqueId(), createdCategory);
                            }
                        }
                );
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
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (category != null && !category.archived()) {
            CategoryJson archivedCategory = new CategoryJson(
                    category.id(),
                    category.name(),
                    category.username(),
                    true
            );
            CategoryApiClient.updateCategory(archivedCategory);
        }
    }
}