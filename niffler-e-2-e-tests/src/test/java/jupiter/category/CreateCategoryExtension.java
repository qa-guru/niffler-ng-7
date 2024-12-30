package jupiter.category;

import api.CategoryApiClient;
import com.github.javafaker.Faker;
import model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CreateCategoryExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateCategoryExtension.class);

    private final CategoryApiClient categoryApiClient = new CategoryApiClient();

    private final Faker faker = new Faker();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
                .ifPresent(annotation -> {

                    CategoryJson categoryJson = new CategoryJson(
                            null,
                            faker.company().industry(),
                            annotation.username(),
                            false
                    );

                    CategoryJson createCategory = categoryApiClient.addCategory(categoryJson);

                    if (annotation.archived()) {
                        CategoryJson archivedCategory = new CategoryJson(
                                createCategory.id(),
                                createCategory.name(),
                                createCategory.username(),
                                true
                        );
                        createCategory = categoryApiClient.updateCategory(archivedCategory);
                    }

                    context.getStore(NAMESPACE).put(context.getUniqueId(), createCategory);
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(CreateCategoryExtension.NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        CategoryJson categoryJson = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (!categoryJson.archived()) {
            CategoryJson archivedJson = new CategoryJson(
                    categoryJson.id(),
                    categoryJson.name(),
                    categoryJson.username(),
                    true
            );
            categoryApiClient.updateCategory(archivedJson);
        }
    }
}
