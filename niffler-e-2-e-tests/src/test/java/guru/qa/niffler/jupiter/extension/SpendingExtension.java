package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.spend.CategoryDBClient;
import guru.qa.niffler.service.spend.SpendDBClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class SpendingExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace
            .create(SpendingExtension.class);

    private final SpendDBClient spendDBClient = new SpendDBClient();
    private final CategoryDBClient categoryDBClient = new CategoryDBClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                        for (Spending annoSpend : userAnno.spendings()) {
                            CategoryJson categoryJson = categoryDBClient
                                    .findCategoryByUsernameAndCategoryName(userAnno.username(), annoSpend.category());
                            SpendJson spend = new SpendJson(
                                    null,
                                    new Date(),
                                    categoryJson != null ? categoryJson :
                                    new CategoryJson(
                                            null,
                                            annoSpend.category(),
                                            userAnno.username(),
                                            false
                                    ),
                                    CurrencyValues.RUB,
                                    annoSpend.amount(),
                                    annoSpend.description(),
                                    userAnno.username()
                                    );
                            SpendJson spendJson = spendDBClient.createSpend(spend);
                            context.getStore(NAMESPACE).put(
                                    context.getUniqueId(),
                                    spendJson
                            );
                            break; // забирает только 1 значение. Убрать если нужно каждое
                        }
                });
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        SpendJson spendJson = context.getStore(NAMESPACE).get(context.getUniqueId(), SpendJson.class);
        if(spendJson != null) {
            spendDBClient.deleteSpend(spendJson);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext,
                                      ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(SpendingExtension.NAMESPACE).get(extensionContext.getUniqueId(), SpendJson.class);
    }

}
