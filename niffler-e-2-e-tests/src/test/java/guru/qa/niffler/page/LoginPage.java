package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.data.LoginPageData;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.support.Color;

import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.*;

public class LoginPage {

    private final SelenideElement loginInput = $(By.name("username"));
    private final SelenideElement passwordInput = $(By.name("password"));
    private final SelenideElement loginButton = $(".form__submit");
    private final SelenideElement createNewAccountButton = $(".form__register");
    private final SelenideElement passwordSubmitInput = $(By.name("passwordSubmit"));
    private final SelenideElement submitButton = $(".form__submit");
    private final SelenideElement confirmRegistrationText = $(".form__paragraph.form__paragraph_success");
    private final SelenideElement headerErrorMessage = $(".header__additional");
    private final SelenideElement loginLink = $(".form__link");
    private final SelenideElement errorMessage = $(".form__error");
    private final SelenideElement loginPagePicture = $(".main__hero");

    @Step("Проверяем получение ошибки про вводе некорректного логина или пароля")
    public LoginPage loginErrorCheck(String badCredentialsMessage){
        assertAll("Bad credentials",
                () -> assertEquals(badCredentialsMessage,errorMessage.getText(),
                        "Сообщение о вводе неверных данных пользователя не отобразилось или текст некорректный"),
                () -> assertEquals(new LoginPageData().getCreatingNewAccountErrorColor(),
                        Color.fromString(errorMessage.getCssValue("color")).asHex().toUpperCase(),
                        "Цвет ошибки некорректный"),
                () -> assertTrue(loginPagePicture.isEnabled(),
                        "После ввода некорректных данных станица изменилась хотя не должна"));
        return this;
    }

    @Step("Проверяем отображение ошибки при регистрации")
    public LoginPage registrationErrorCheck(
            String headerErrorText,
            String linkForLogIn,
            String userNameExistsErrorMessageText,
            String userNameExistsErrorMessageColor){
        assertAll("Registration error",
                () -> assertEquals(headerErrorText, headerErrorMessage.getText(),
                        "Сообщение о ошибке не отображается"),
                () -> assertEquals(linkForLogIn, loginLink.getAttribute("href"),
                        "Ссылка для перехода на страницу авторизации некорректна"),
                () -> assertEquals(userNameExistsErrorMessageText, errorMessage.getText(),
                        "Сообщение о том что userName уже существует не появилось или некорретно"),
                () -> assertEquals(userNameExistsErrorMessageColor,
                        Color.fromString(errorMessage.getCssValue("color")).asHex().toUpperCase(),
                        "Цвет сообщение о том, что userName уже используется некорректен"));
        return this;
    }

    @Step("Проверяем подтверждение регистрации")
    public LoginPage confirmRegistrationCheck(String expected) {
        assertEquals(expected, confirmRegistrationText.getText(),
                "Регистрация нового пользователя была неуспешна");
        return this;
    }

    @Step("Подтверждаем создание нового пользователя")
    public LoginPage submitButtonClick() {
        submitButton.click();
        return this;
    }

    @Step("Вводим значение в поле Submit your password")
    public LoginPage setPasswordSubmit(String value) {
        passwordSubmitInput.setValue(value);
        return this;
    }

    @Step("Нажимаем кнопку Create new account")
    public LoginPage createNewAccountButtonClick() {
        createNewAccountButton.click();
        return this;
    }

    @Step("Вводим значение в поле username")
    public LoginPage setUserName(String value) {
        loginInput.setValue(value);
        return this;
    }

    @Step("Вводим значение в поле password")
    public LoginPage setPassword(String value) {
        passwordInput.setValue(value);
        return this;
    }

    @Step("Открываем страницу авторизации ")
    public LoginPage openAuthorizationPage(String url) {
        Selenide.open(url);
        return this;
    }

    @Step("Вводим значение в Input {name}")
    public LoginPage setAuthorizationInput(String value, @NotNull String name) {
        switch (name) {
            case "login":
                loginInput.setValue(value);
                break;
            case "password":
                passwordInput.setValue(value);
                break;
            default:
                throw new RuntimeException("Указанного Input нет на странице");
        }
        return this;
    }

    @Step("Нажимаем кнопку Log In")
    public LoginPage loginButtonClick() {
        loginButton.click();
        return this;
    }
}
