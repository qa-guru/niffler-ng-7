package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.jupiter.Spend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class SpendingWebTest {
    private static final Config CFG = Config.getInstance();

    @Spend(
            category = "Учеба",
            description = "Обучение",
            username = "duck",
            amount = 1000.0,
            currency = CurrencyValues.RUB
    )
    @Test
    void categoryDescriptionShouldBeEditedByTableAction(SpendJson spend) {
        final String newSpendingName = "Обучение NG";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin("duck", "12345")
                .findSpending(spend.description())
                .editSpendingDescription(newSpendingName);

        new MainPage().checkThatTableContains(newSpendingName);
    }
}
