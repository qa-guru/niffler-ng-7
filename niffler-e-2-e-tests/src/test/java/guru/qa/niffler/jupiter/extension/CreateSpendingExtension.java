package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.jupiter.annotation.Spend;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class CreateSpendingExtension implements BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateSpendingExtension.class);

    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Spend.class)
                .ifPresent(anno -> {
                    SpendJson spendJson = new SpendJson(
                            null,
                            new Date(),
                            new CategoryJson(
                                    null,
                                    anno.category(),
                                    anno.username(),
                                    false
                            ),
                            anno.currency(),
                            anno.amount(),
                            anno.description(),
                            anno.username()
                    );

                    SpendJson createdSpend = spendApiClient.createSpend(spendJson);

                    context.getStore(NAMESPACE).put(
                            context.getUniqueId(),
                            createdSpend
                    );
                });
    }

}
