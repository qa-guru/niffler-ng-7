package student.jupiter.extension.spending;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import student.jupiter.annotaion.meta.User;
import student.model.CategoryJson;
import student.model.CurrencyValues;
import student.model.SpendJson;
import student.service.SpendDbClient;

import java.sql.SQLException;
import java.util.Date;

import static student.util.CategoryHelper.randomCategoryName;

public class CreateSpendingExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateSpendingExtension.class);

    SpendDbClient spendDbClient = new SpendDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport
                .findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(annotation -> {
                    if (ArrayUtils.isNotEmpty(annotation.spendings())) {
                        var spending = annotation.spendings()[0];
                        var category = annotation.categories()[0];
                        var categoryJson = new CategoryJson(
                                null,
                                randomCategoryName(),
                                annotation.username(),
                                category.archived()
                        );
                        SpendJson spendJson = new SpendJson(
                                null,
                                new Date(),
                                categoryJson,
                                CurrencyValues.RUB,
                                spending.amount(),
                                spending.description(),
                                annotation.username()
                        );
                        try {
                            context.getStore(NAMESPACE).put(
                                    context.getUniqueId(),
                                    spendDbClient.createSpend(spendJson)
                            );
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(CreateSpendingExtension.NAMESPACE).get(extensionContext.getUniqueId(), SpendJson.class);
    }
}
