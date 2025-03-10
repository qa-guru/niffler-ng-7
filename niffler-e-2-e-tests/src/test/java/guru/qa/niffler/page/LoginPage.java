package guru.qa.niffler.page;


import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;


@ParametersAreNonnullByDefault
public class LoginPage extends BasePage<LoginPage> {

    public static final String URL = CFG.authUrl() + "login";

    private final SelenideElement
            userNameInput,
            passwordInput,
            formRegisterButton,
            errorForm;

    public LoginPage(SelenideDriver driver) {
        super(driver);
        this.userNameInput = driver.$("input[name='username']");
        this.passwordInput = driver.$("input[name='password']");
        this.formRegisterButton = driver.$(".form__register");
        this.errorForm = driver.$(".form__error");
    }

    public LoginPage() {
        this.userNameInput = $("input[name='username']");
        this.passwordInput = $("input[name='password']");
        this.formRegisterButton = $(".form__register");
        this.errorForm = $(".form__error");
    }

    @Override
    @Nonnull
    public LoginPage checkThatPageLoaded() {
        userNameInput.should(visible);
        passwordInput.should(visible);
        return this;
    }

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