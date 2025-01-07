package guru.qa.niffler.helpers.jupiter.extension;

import guru.qa.niffler.dataBase.service.SpendDbClient;
import guru.qa.niffler.helpers.jupiter.annotation.CreatingSpend;
import guru.qa.niffler.helpers.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendingJson;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.*;

import java.util.Date;


public class CreatingSpending implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreatingSpending.class);
    private final SpendDbClient spendDbClient = new SpendDbClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        User user = context.getRequiredTestMethod().getAnnotation(User.class);

        if (user.spendings().length > 0) {

            var category = spendDbClient.findCategoryByUsernameAndCategoryName(user.username(), user.spendings()[0].spendCategory());
            var spendEntityList = spendDbClient.findAllSpendsByUserName(user.username());

            if (spendEntityList != null) {
                for (int i = 0; i < spendEntityList.size(); i++) {
                    spendDbClient.deleteSpend(
                            new SpendingJson(
                                    spendEntityList.get(i).getId(),
                                    spendEntityList.get(i).getSpendDate(),
                                    new CategoryJson(
                                            spendEntityList.get(i).getCategory().getId(),
                                            spendEntityList.get(i).getCategory().getName(),
                                            spendEntityList.get(i).getCategory().getUsername(),
                                            false
                                    ),
                                    spendEntityList.get(i).getCurrency(),
                                    spendEntityList.get(i).getAmount(),
                                    spendEntityList.get(i).getDescription(),
                                    spendEntityList.get(i).getUsername()

                            )
                    );
                }
                spendDbClient.deleteCategory(
                        new CategoryJson(
                                category.get().id(),
                                category.get().name(),
                                category.get().username(),
                                category.get().archived()
                        )
                );
            }

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

            var response = spendDbClient.createSpend(spendingJson);
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
