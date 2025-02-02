package student.jupiter.extension.category;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import student.jupiter.annotaion.meta.User;
import student.model.CategoryJson;
import student.service.SpendDbClient;

import java.sql.SQLException;

import static student.util.CategoryHelper.randomCategoryName;

public class CreateCategoryExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateCategoryExtension.class);

    SpendDbClient spendDbClient = new SpendDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport
                .findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(annotation -> {
                    if (ArrayUtils.isNotEmpty(annotation.categories())) {
                        var category = annotation.categories()[0];
                        CategoryJson categoryJson = new CategoryJson(
                                null,
                                randomCategoryName(),
                                annotation.username(),
                                category.archived()
                        );
                        CategoryJson createCategory;
                        try {
                            createCategory = spendDbClient.createCategory(categoryJson);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
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
    public void afterTestExecution(ExtensionContext context) throws SQLException {
        CategoryJson categoryJson = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (categoryJson != null) {
            CategoryJson category = new CategoryJson(
                    categoryJson.id(),
                    categoryJson.name(),
                    categoryJson.username(),
                    categoryJson.archived()
            );
            spendDbClient.deleteSpendByCategoryId(categoryJson.id());
            spendDbClient.deleteCategory(category);
        }
    }
}
