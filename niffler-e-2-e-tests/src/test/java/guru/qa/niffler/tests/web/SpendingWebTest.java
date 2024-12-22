package guru.qa.niffler.tests.web;

import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.jupiter.Spending;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class SpendingWebTest {


    @Spending(
            username = "Artur",
            category = "Обучение",
            description = "Обучение Advanced 2.0",
            amount = 79990
    )
    @Test
    void categoryDescriptionShouldBeChangedFromTable(SpendJson spend) {
        final String newDescription = "Обучение Niffler Next Generation";

        new LoginPage()
                .open()
                .login("Artur", "12345")
                .editSpending(spend.description())
                .setDescription(newDescription)
                .save();

        new MainPage().checkThatTableContainsSpending(newDescription);
    }
}