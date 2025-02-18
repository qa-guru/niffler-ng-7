package guru.qa.niffler.page;


import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.interactable;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class LoginPage {
    private final SelenideElement
            userNameInput = $("input[name='username']"),
            passwordInput = $("input[name='password']"),
            formRegisterButton = $(".form__register"),
            errorForm = $(".form__error");

    @Step("Нажимаем на кнопку 'Регистрация'")
    public RegisterPage clickOnRegisterButton() {
        formRegisterButton.click();
        return new RegisterPage();
    }

    @Step("Аторизуемся пользователе '{0}'")
    @Nonnull
    public MainPage doLogin(String userName, String password) {
        setUserName(userName);
        setPassword(password);
        return new MainPage();
    }

    @Step("Вводим логин '{0}'")
    @Nonnull
    public LoginPage setUserName(String userName) {
        userNameInput.shouldBe(interactable).setValue(userName);
        return this;
    }

    @Step("Вводим пароль '{0}'")
    @Nonnull
    public LoginPage setPassword(String userName) {
        passwordInput.setValue(userName).pressEnter();
        return this;
    }

    @Step("Проверка ошибки при авторизации")
    @Nonnull
    public LoginPage checkBadCredentialError() {
        errorForm.shouldHave(text("Неверные учетные данные пользователя"));
        return this;
    }
}