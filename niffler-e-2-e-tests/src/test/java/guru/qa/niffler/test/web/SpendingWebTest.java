package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static guru.qa.niffler.utils.RandomDataUtils.randomSentence;

@WebTest

public class SpendingWebTest {

    private static final Config CFG = Config.getInstance();

    @User(
            spendings = @Spending(
                    category = "Food",
                    description = "apples",
                    amount = 50
            )
    )
    @Test
    void categoryDescriptionShouldBeChangedFromTable(UserJson user) {
        final String newDescription = "bananas";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), "123")
                .editSpending("apples")
                .setDescription(newDescription)
                .save()
                .checkAlertMessage("Spending is edited successfully");
        ;

        new MainPage().checkThatTableContainsSpending(newDescription);
    }

    @User
    @Test
    void shouldAddNewSpending(UserJson user) {
        String category = "Food";
        int amount = 100;
        Date currentDate = new Date();
        String description = randomSentence(1);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .checkIsLoaded()
                .getHeader()
                .addSpendingPage()
                .setSpendingAmount(amount)
                .setCategory(category)
                .setSpendingDate(currentDate)
                .setDescription(description)
                .save()
                .checkAlertMessage("New spending is successfully created");

        new MainPage().getSpendingTable()
                .checkTableContains(description);
    }
}