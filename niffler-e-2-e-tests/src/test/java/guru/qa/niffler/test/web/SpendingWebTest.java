package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.jupiter.annotation.Spend;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class SpendingWebTest {

    private static final Config CFG = Config.getInstance();

    @Spend(
            category = "Food",
            description = "Tuna salad",
            username = "ivan",
            amount = 2000,
            currency = CurrencyValues.RUB
    )
    @Test
    public void categoryDescriptionShouldBeEditedByTableAction(SpendJson spend) {
        final String newSpendingDescription = "Super Food";
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin("ivan", "12345")
                .editSpending(spend.description())
                .setDescription(newSpendingDescription);

        new MainPage().checkThatTableContains(newSpendingDescription);
    }
}
