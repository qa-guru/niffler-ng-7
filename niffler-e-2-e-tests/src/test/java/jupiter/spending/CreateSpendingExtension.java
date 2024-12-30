package jupiter.spending;

import api.SpendApiClient;
import model.CategoryJson;
import model.Currency;
import model.SpendJson;
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
