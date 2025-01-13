package student.jupiter.extension.spending;

import student.api.SpendApiClient;
import student.jupiter.annotaion.Spending;
import student.model.CategoryJson;
import student.model.Currency;
import student.model.SpendJson;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class CreateSpendingExtension implements BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateSpendingExtension.class);

    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Spending.class)
                .ifPresent(annotation -> {
                    SpendJson spend = new SpendJson(
                            null,
                            new Date(),
                            new CategoryJson(
                                    null,
                                    annotation.category(),
                                    annotation.username(),
                                    false
                            ),
                            Currency.RUB,
                            annotation.amount(),
                            annotation.description(),
                            annotation.username()
                    );
                    context.getStore(NAMESPACE).put(
                            context.getUniqueId(),
                            spendApiClient.createSpend(spend)
                    );
                });
    }
}
