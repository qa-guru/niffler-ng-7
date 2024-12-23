package quru.qa.niffler.jupiter.spend;


import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import quru.qa.niffler.api.spend.SpendApiClient;
import quru.qa.niffler.model.CategoryJson;
import quru.qa.niffler.model.CurrencyValues;
import quru.qa.niffler.model.SpendJson;

import java.util.Date;

public class CreateSpendingExtension implements BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateSpendingExtension.class);

    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Spending.class)
                .ifPresent(anno -> {
                    SpendJson spend = new SpendJson(
                            null,
                            new Date(),
                            new CategoryJson(
                                    null,
                                    anno.category(),
                                    anno.username(),
                                    false
                            ),
                            CurrencyValues.RUB,
                            anno.amount(),
                            anno.description(),
                            anno.username()
                    );
                    SpendJson createdSpend = spendApiClient.createSpend(spend);
                    context.getStore(NAMESPACE).put(context.getUniqueId(), createdSpend);
                });
    }

}
