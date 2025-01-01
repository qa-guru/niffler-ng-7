package guru.qa.niffler.test;

import guru.qa.niffler.data.LoginPageData;
import guru.qa.niffler.data.UserData;
import guru.qa.niffler.helpers.dataGeneration.FioGeneration;
import guru.qa.niffler.helpers.dataGeneration.NewAccountDataGeneration;
import guru.qa.niffler.helpers.jupiter.annotation.Category;
import guru.qa.niffler.helpers.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@ExtendWith(BrowserExtension.class)
public class WebTest {

    LoginPage loginPage = new LoginPage();
    MainPage mainPage = new MainPage();
    EditSpendingPage editSpendingPage = new EditSpendingPage();
    NewAccountDataGeneration newAccountDataGeneration = new NewAccountDataGeneration();
    ProfilePage profilePage = new ProfilePage();

//    @CreatingSpend(
//            spendName = "stady",
//            description = "hello",
//            amount = 1000,
//            currency = CurrencyValues.RUB,
//            spendCategory = "stady"
//    )
//    @Test
//    @DisplayName("Проверка возможности изменить Description траты")
//    @Owner("Казанцев Иван")
//    @Severity(SeverityLevel.CRITICAL)
//    void editSpendingDescriptionCheck() {
//        loginPage.openAuthorizationPage(new LoginPageData().getUrl())
//                .setAuthorizationInput(new UserData().getLogin(), "login")
//                .setAuthorizationInput(new UserData().getPassword(), "password")
//                .loginButtonClick();
//        mainPage.historyOfSpendingTextCheck(new MainPageData().getHistoryOfSpendingText())
//                .searchSpend("123")
//                .editButtonClick();
//        editSpendingPage.pageOenCheck(new EditSpendingPageData().getPageTitle())
//                .setDescription(SpendData.spend.DESCRIPTION.getValue())
//                .saveButtonClick();
//        mainPage.historyOfSpendingTextCheck(new MainPageData().getHistoryOfSpendingText())
//                .searchSpend(SpendData.spend.DESCRIPTION.getValue())
//                .resultTableCheck(SpendData.spend.values());
//    }
//
//    @CreatingSpend(
//            spendName = "stady",
//            description = "Тест удаления",
//            amount = 1000,
//            currency = CurrencyValues.RUB,
//            spendCategory = "stady"
//    )
//    @Test
//    @DisplayName("Проверка возможности удалить траты")
//    @Owner("Казанцев Иван")
//    @Severity(SeverityLevel.CRITICAL)
//    void deleteSpendingDescriptionCheck(@NotNull SpendingJson spendingJson) {
//        loginPage.openAuthorizationPage(new LoginPageData().getUrl())
//                .setAuthorizationInput(new UserData().getLogin(), "login")
//                .setAuthorizationInput(new UserData().getPassword(), "password")
//                .loginButtonClick();
//        mainPage.historyOfSpendingTextCheck(new MainPageData().getHistoryOfSpendingText())
//                .searchSpend(spendingJson.description())
//                .chooseSpend()
//                .delButtonClick()
//                .acceptDelButtonClick()
//                .searchSpend(spendingJson.description())
//                .resultTableShouldBeNotVisibleCheck();
//
//    }

    @Test
    @DisplayName("Проверка регистрации нового пользователя")
    @Owner("Казацнев Иван")
    @Severity(SeverityLevel.BLOCKER)
    void registrationNewAccountCheck() {
        loginPage.openAuthorizationPage(new LoginPageData().getUrl())
                .createNewAccountButtonClick()
                .setUserName(new FioGeneration().getUserName())
                .setPassword(newAccountDataGeneration.getRandomPassword())
                .setPasswordSubmit(newAccountDataGeneration.getRandomPassword())
                .submitButtonClick()
                .confirmRegistrationCheck(new LoginPageData().getSuccessRegistrationText());
    }

    @Test
    @DisplayName("Проверка регистарации при вводе уже существующего пользователя")
    @Owner("Казацнев Иван")
    @Severity(SeverityLevel.CRITICAL)
    void registrationWithExistingUserNameCheck() {
        loginPage.openAuthorizationPage(new LoginPageData().getUrl())
                .createNewAccountButtonClick()
                .setUserName(new UserData().getLogin())
                .setPassword(newAccountDataGeneration.getRandomPassword())
                .setPasswordSubmit(newAccountDataGeneration.getRandomPassword())
                .submitButtonClick()
                .registrationErrorCheck(
                        new LoginPageData().getRefusalToRegisterText(),
                        new LoginPageData().getLoginHref(),
                        new LoginPageData().getCreatingNewAccountError(),
                        new LoginPageData().getCreatingNewAccountErrorColor());
    }

    @Test
    @DisplayName("Проверка регистрации при ввода в поле 'Подтвердите пароль' некорректного пароля")
    @Owner("Казацнев Иван")
    @Severity(SeverityLevel.BLOCKER)
    void registrationIfPasswordAndConfirmPasswordAreNotEquals() {
        loginPage.openAuthorizationPage(new LoginPageData().getUrl())
                .createNewAccountButtonClick()
                .setUserName(new FioGeneration().getUserName())
                .setPassword(newAccountDataGeneration.getRandomPassword())
                .setPasswordSubmit(new NewAccountDataGeneration().getRandomPassword())
                .submitButtonClick()
                .registrationErrorCheck(
                        new LoginPageData().getRefusalToRegisterText(),
                        new LoginPageData().getLoginHref(),
                        new LoginPageData().getConfirmPasswordErrorText(),
                        new LoginPageData().getCreatingNewAccountErrorColor());
    }

    @Test
    @DisplayName("Проверка успешного логина")
    @Owner("Казанцев Иван")
    @Severity(SeverityLevel.BLOCKER)
    void successLoginInCheck() {
        loginPage.openAuthorizationPage(new LoginPageData().getUrl())
                .setUserName(new UserData().getLogin())
                .setPassword(new UserData().getPassword())
                .loginButtonClick();
        mainPage.pageLoadCheck();
    }

    @ParameterizedTest
    @EnumSource(LoginPageData.invalidUserData.class)
    @DisplayName("Проверка появление сообщение о ошибке при вводе не валидных данных")
    @Owner("Казанцев Иван")
    @Severity(SeverityLevel.BLOCKER)
    void loginWithBadCredentials(@NotNull LoginPageData.invalidUserData data) {
        loginPage.openAuthorizationPage(new LoginPageData().getUrl())
                .setUserName(data.getName())
                .setPassword(data.getPasswordValue())
                .loginButtonClick()
                .loginErrorCheck(new LoginPageData().getBadCredentialsText());
    }

    @Category(
            username = "ivan",
            archived = false
    )
    @Test
    @DisplayName("Проверка отображения активной категории")
    @Owner("Казанцев Иван")
    @Severity(SeverityLevel.CRITICAL)
    void archiveCategoryMustBeVisible(@NotNull CategoryJson categoryJson){
        loginPage.openAuthorizationPage(new LoginPageData().getUrl())
                .setUserName(new UserData().getLogin())
                .setPassword(new UserData().getPassword())
                .loginButtonClick();
        mainPage.moveToUserProfile();
        profilePage.activeCategoryVisibleCheck(categoryJson.name())
                .makeCategoryArchived(categoryJson.name())
                .archivedCategoryDoNotVisibleInActiveCategoryTableCheck(categoryJson.name());
    }

    @Category(
            username = "ivan",
            archived = true
    )
    @Test
    @DisplayName("Проверка отображения архивной категории")
    @Owner("Казанцев Иван")
    @Severity(SeverityLevel.CRITICAL)
    void archiveCategoryMustBeNotVisible(@NotNull CategoryJson categoryJson){
        loginPage.openAuthorizationPage(new LoginPageData().getUrl())
                .setUserName(new UserData().getLogin())
                .setPassword(new UserData().getPassword())
                .loginButtonClick();
        mainPage.moveToUserProfile();
        profilePage.archivedCategoryDoNotVisibleInActiveCategoryTableCheck(categoryJson.name())
                .showArchivedToggleSwitchClick()
                .activeCategoryVisibleCheck(categoryJson.name());
    }
}
