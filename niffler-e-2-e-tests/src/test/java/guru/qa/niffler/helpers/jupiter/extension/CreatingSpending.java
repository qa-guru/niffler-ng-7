package guru.qa.niffler.helpers.jupiter.extension;

import guru.qa.niffler.helpers.api.SpendApiClient;
import guru.qa.niffler.helpers.jupiter.annotation.CreatingSpend;
import guru.qa.niffler.helpers.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendingJson;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.*;

import java.util.Date;


public class CreatingSpending implements BeforeEachCallback, ParameterResolver {

    private final SpendApiClient spendApiClient = new SpendApiClient();
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreatingSpending.class);
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        User user = context.getRequiredTestMethod().getAnnotation(User.class);
        if (user != null) {
            SpendingJson spendingJson = new SpendingJson(
                    null,
                    new Date(),
                    new CategoryJson(
                            null,
                            user.spendings()[0].spendName(),
                            user.username(),
                            false
                    ),
                    user.spendings()[0].currency(),
                    user.spendings()[0].amount(),
                    user.spendings()[0].description(),
                    user.username()
            );

            var response = spendApiClient.createSpend(spendingJson);
            System.out.println(response);
            context.getStore(NAMESPACE).put(
                    context.getUniqueId(),
                    response
            );
            context.getStore(NAMESPACE).put(CreatingSpend.class, spendingJson);
        }
    }

    @Override
    public boolean supportsParameter(@NotNull ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendingJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(CreatingSpend.class);
    }
}
