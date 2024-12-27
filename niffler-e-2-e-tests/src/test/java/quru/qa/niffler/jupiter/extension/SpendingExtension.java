package quru.qa.niffler.jupiter.extension;


import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import quru.qa.niffler.api.spend.SpendApiClient;
import quru.qa.niffler.jupiter.annotation.Spending;
import quru.qa.niffler.model.CategoryJson;
import quru.qa.niffler.model.CurrencyValues;
import quru.qa.niffler.model.SpendJson;

import java.util.Date;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);

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

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), SpendJson.class);
    }

}
