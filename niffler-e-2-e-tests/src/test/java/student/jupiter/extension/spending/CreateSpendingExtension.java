package student.jupiter.extension.spending;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import student.api.SpendApiClient;
import student.jupiter.annotaion.meta.User;
import student.model.CategoryJson;
import student.model.Currency;
import student.model.SpendJson;

import java.util.Date;

public class CreateSpendingExtension implements BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateSpendingExtension.class);

    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport
                .findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(annotation -> {
                    if (ArrayUtils.isNotEmpty(annotation.spendings())) {
                        var spending = annotation.spendings()[0];
                        var category = annotation.categories()[0];
                        SpendJson spend = new SpendJson(
                                null,
                                new Date(),
                                new CategoryJson(
                                        null,
                                        spending.category(),
                                        annotation.username(),
                                        category.archived()
                                ),
                                Currency.RUB,
                                spending.amount(),
                                spending.description(),
                                annotation.username()
                        );
                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                spendApiClient.createSpend(spend)
                        );
                    }

                });
    }
}
