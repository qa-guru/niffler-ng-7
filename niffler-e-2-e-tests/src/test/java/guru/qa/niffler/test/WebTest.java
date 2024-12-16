package guru.qa.niffler.test;

import guru.qa.niffler.data.*;
import guru.qa.niffler.helpers.jupiter.annotation.CreatingSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendingJson;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WebTest {

    LoginPage loginPage = new LoginPage();
    MainPage mainPage = new MainPage();
    EditSpendingPage editSpendingPage = new EditSpendingPage();


    @CreatingSpend(
            spendName = "stady",
            description = "hello",
            amount = 1000,
            currency = CurrencyValues.RUB,
            spendCategory = "stady"
    )
    @Test
    @DisplayName("Проверка возможности изменить Description траты")
    @Owner("Казанцев Иван")
    @Severity(SeverityLevel.CRITICAL)
    void editSpendingDescriptionCheck(@NotNull SpendingJson spendingJson) {
        loginPage.openAuthorizationPage(new LoginPageData().getUrl())
                .setAuthorizationInput(new UserData().getLogin(), "login")
                .setAuthorizationInput(new UserData().getPassword(), "password")
                .loginButtonClick();
        mainPage.historyOfSpendingTextCheck(new MainPageData().getHistoryOfSpendingText())
                .searchSpend(spendingJson.description())
                .editButtonClick();
        editSpendingPage.pageOenCheck(new EditSpendingPageData().getPageTitle())
                .setDescription(SpendData.spend.DESCRIPTION.getValue())
                .saveButtonClick();
        mainPage.historyOfSpendingTextCheck(new MainPageData().getHistoryOfSpendingText())
                .searchSpend(SpendData.spend.DESCRIPTION.getValue())
                .resultTableCheck(SpendData.spend.values());
    }

    @CreatingSpend(
            spendName = "stady",
            description = "Тест удаления",
            amount = 1000,
            currency = CurrencyValues.RUB,
            spendCategory = "stady"
    )
    @Test
    @DisplayName("Проверка возможности удалить траты")
    @Owner("Казанцев Иван")
    @Severity(SeverityLevel.CRITICAL)
    void deleteSpendingDescriptionCheck(@NotNull SpendingJson spendingJson) {
        loginPage.openAuthorizationPage(new LoginPageData().getUrl())
                .setAuthorizationInput(new UserData().getLogin(), "login")
                .setAuthorizationInput(new UserData().getPassword(), "password")
                .loginButtonClick();
        mainPage.historyOfSpendingTextCheck(new MainPageData().getHistoryOfSpendingText())
                .searchSpend(spendingJson.description())
                .chooseSpend()
                .delButtonClick()
                .acceptDelButtonClick()
                .searchSpend(spendingJson.description())
                .resultTableShouldBeNotVisibleCheck();

    }
}
