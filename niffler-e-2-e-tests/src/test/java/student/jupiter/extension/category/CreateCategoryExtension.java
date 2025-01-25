package student.jupiter.extension.category;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import student.api.CategoryApiClient;
import student.jupiter.annotaion.meta.User;
import student.model.CategoryJson;
import student.util.RandomDataUtils;

import static student.util.DataGenerator.category;

public class CreateCategoryExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateCategoryExtension.class);

    private final CategoryApiClient categoryApiClient = new CategoryApiClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport
                .findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(annotation -> {
                    if (ArrayUtils.isNotEmpty(annotation.categories())) {
                        var category = annotation.categories()[0];
                        CategoryJson categoryJson = new CategoryJson(
                                null,
                                RandomDataUtils.randomCategoryName(),
                                annotation.username(),
                                category.archived()
                        );

                        CategoryJson createCategory = categoryApiClient.addCategory(categoryJson);

                        if (category.archived()) {
                            CategoryJson archivedCategory = new CategoryJson(
                                    createCategory.id(),
                                    createCategory.name(),
                                    createCategory.username(),
                                    true
                            );
                            createCategory = categoryApiClient.updateCategory(archivedCategory);
                        }

                        context.getStore(NAMESPACE).put(context.getUniqueId(), createCategory);
                    }
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
        if (category != null && !categoryJson.archived()) {
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
