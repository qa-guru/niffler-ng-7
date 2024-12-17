package quru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import quru.qa.niffler.config.Config;
import quru.qa.niffler.jupiter.Spending;
import quru.qa.niffler.model.SpendJson;
import quru.qa.niffler.page.LoginPage;
import quru.qa.niffler.page.MainPage;

import static com.codeborne.selenide.Selenide.sleep;

public class SpendingWebTest {

    private static final Config CFG = Config.getInstance();

    @Spending(
            username = "filkot",
            category = "Обучение",
            description = "Обучение Advanced 2.0",
            amount = 79990
    )
    @Test
    void categoryDescriptionShouldBeChangedFromTable(SpendJson spend) {
        final String newDescription = "Обучение Niffler Next Generation";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("filkot", "12345")
                .editSpending(spend.description())
                .setNewSpendingDescription(newDescription)
                .save();

        sleep(5000);

        new MainPage().checkThatTableContainsSpending(newDescription);
    }
}
