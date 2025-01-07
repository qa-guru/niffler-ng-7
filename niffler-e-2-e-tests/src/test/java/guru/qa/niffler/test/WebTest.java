package guru.qa.niffler.test;

import guru.qa.niffler.data.*;

import guru.qa.niffler.helpers.dataGeneration.NewAccountDataGeneration;
import guru.qa.niffler.helpers.dataGeneration.RandomDataUtils;
import guru.qa.niffler.helpers.jupiter.annotation.Category;
import guru.qa.niffler.helpers.jupiter.annotation.CreatingSpend;
import guru.qa.niffler.helpers.jupiter.annotation.User;
import guru.qa.niffler.helpers.jupiter.annotation.UserType;
import guru.qa.niffler.helpers.jupiter.extension.BrowserExtension;
import guru.qa.niffler.helpers.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendingJson;
import guru.qa.niffler.page.*;
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
    FriendsPage friendsPage = new FriendsPage();


    @User(
            username = "ivan",
            spendings = @CreatingSpend(
                    spendName = "stady",
                    description = "hello",
                    amount = 1000,
                    currency = CurrencyValues.RUB,
                    spendCategory = "stady"
            )
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


    @User(
            username = "ivan",
            spendings = @CreatingSpend(
                    spendName = "stady",
                    description = "Тест удаления",
                    amount = 1000,
                    currency = CurrencyValues.RUB,
                    spendCategory = "stady"
            )
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

    @Test
    @DisplayName("Проверка регистрации нового пользователя")
    @Owner("Казацнев Иван")
    @Severity(SeverityLevel.BLOCKER)
    void registrationNewAccountCheck() {
        loginPage.openAuthorizationPage(new LoginPageData().getUrl())
                .createNewAccountButtonClick()
                .setUserName(RandomDataUtils.getUserName())
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
                .setUserName(RandomDataUtils.getUserName())
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

    @User(
            username = "ivan",
            categories = @Category(
                    archived = false
            )
    )
    @Test
    @DisplayName("Проверка отображения активной категории")
    @Owner("Казанцев Иван")
    @Severity(SeverityLevel.CRITICAL)
    void archiveCategoryMustBeVisible(@NotNull CategoryJson categoryJson) {
        loginPage.openAuthorizationPage(new LoginPageData().getUrl())
                .setUserName(new UserData().getLogin())
                .setPassword(new UserData().getPassword())
                .loginButtonClick();
        mainPage.moveToUserProfile();
        profilePage.activeCategoryVisibleCheck(categoryJson.name())
                .makeCategoryArchived(categoryJson.name())
                .archivedCategoryDoNotVisibleInActiveCategoryTableCheck(categoryJson.name());
    }

    @User(
            username = "ivan",
            categories = @Category(
                    archived = true
            )
    )
    @Test
    @DisplayName("Проверка отображения архивной категории")
    @Owner("Казанцев Иван")
    @Severity(SeverityLevel.CRITICAL)
    void archiveCategoryMustBeNotVisible(@NotNull CategoryJson categoryJson) {
        loginPage.openAuthorizationPage(new LoginPageData().getUrl())
                .setUserName(new UserData().getLogin())
                .setPassword(new UserData().getPassword())
                .loginButtonClick();
        mainPage.moveToUserProfile();
        profilePage.archivedCategoryDoNotVisibleInActiveCategoryTableCheck(categoryJson.name())
                .showArchivedToggleSwitchClick()
                .activeCategoryVisibleCheck(categoryJson.name());
    }

    @Test
    @DisplayName("Проверка таблицы друзей когда друзей нет")
    @Owner("Казанцев Иван")
    @Severity(SeverityLevel.CRITICAL)
    @ExtendWith(UserQueueExtension.class)
    void emptyFriendTableCheck(@NotNull @UserType(UserType.Type.EMPTY) UserQueueExtension.StaticUser user) {
        loginPage.openAuthorizationPage(new LoginPageData().getUrl())
                .setUserName(user.username())
                .setPassword(user.password())
                .loginButtonClick();
        mainPage.moveToFriendsPage();
        friendsPage.tableCheck(new FriendsPageData().getEmptyTableText(), UserType.Type.EMPTY);
    }

    @Test
    @DisplayName("Проверка таблицы друзей когда друзей есть")
    @Owner("Казанцев Иван")
    @Severity(SeverityLevel.CRITICAL)
    @ExtendWith(UserQueueExtension.class)
    void notEmptyFriendTableCheck(@NotNull @UserType(UserType.Type.WITH_FRIEND) UserQueueExtension.StaticUser user) {
        loginPage.openAuthorizationPage(new LoginPageData().getUrl())
                .setUserName(user.username())
                .setPassword(user.password())
                .loginButtonClick();
        mainPage.moveToFriendsPage();
        friendsPage.tableCheck(user.friend(), UserType.Type.WITH_FRIEND);
    }

    @Test
    @DisplayName("Проверка таблицы у пользователя с заявкой в друзья ")
    @Owner("Казанцев Иван")
    @Severity(SeverityLevel.CRITICAL)
    @ExtendWith(UserQueueExtension.class)
    void haveRequestFriendTableCheck(@NotNull @UserType(UserType.Type.WITH_SEND_REQUEST) UserQueueExtension.StaticUser user) {
        loginPage.openAuthorizationPage(new LoginPageData().getUrl())
                .setUserName(user.username())
                .setPassword(user.password())
                .loginButtonClick();
        mainPage.moveToFriendsPage();
        friendsPage.tableCheck(user.sendRequest(), UserType.Type.WITH_SEND_REQUEST);
    }

    @Test
    @DisplayName("Проверка таблицы у пользователя с отправленной заявкой в друзья ")
    @Owner("Казанцев Иван")
    @Severity(SeverityLevel.CRITICAL)
    @ExtendWith(UserQueueExtension.class)
    void sendRequestFriendTableCheck(@NotNull @UserType(UserType.Type.WITH_GET_REQUEST) UserQueueExtension.StaticUser user) {
        loginPage.openAuthorizationPage(new LoginPageData().getUrl())
                .setUserName(user.username())
                .setPassword(user.password())
                .loginButtonClick();
        mainPage.moveToFriendsPage();
        friendsPage.switchForAllPeopleTable()
                .tableCheck(user.sendRequest(), UserType.Type.WITH_GET_REQUEST);
    }
}
