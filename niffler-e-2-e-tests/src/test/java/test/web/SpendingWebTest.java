package test.web;

import com.codeborne.selenide.Selenide;
import config.Config;
import jupiter.browser.BrowserExtension;
import jupiter.spending.Spending;
import model.SpendJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.LoginPage;
import pages.MainPage;

import static util.DataGenerator.userName;
import static util.DataGenerator.userPassword;

@ExtendWith(BrowserExtension.class)
public class SpendingWebTest {

    private static final Config CFG = Config.getInstance();

    @Spending(
            username = userName,
            category = "Car",
            description = "NEW",
            amount = 4000.0
    )
    @Test
    void categoryDescriptionShouldBeChangedFromTable(SpendJson spend) {
        final String newDescription = "Обучение Niffler Next Generation";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setUserName(userName)
                .setPassword(userPassword)
                .clickSubmitButton()
                .editSpending(spend.description())
                .setDescription(newDescription)
                .save();

        new MainPage().checkThatTableContainsSpending(newDescription);
    }
}
