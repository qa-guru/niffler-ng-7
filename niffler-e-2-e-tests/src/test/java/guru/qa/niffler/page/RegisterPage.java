package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class RegisterPage {
    private final SelenideElement
            usernameInput = $("input[id='username']"),
            passwordInput = $("input[id='password']"),
            submitPasswordInput = $("input[id='passwordSubmit']"),
            submitRegistrationButton = $("button[type='submit']"),
            formParagraphSuccess = $(".form__paragraph_success"),
            signInButton = $(".form_sign-in"),
            errorMessage = $(".form__error");

    @Nonnull
    @Step("Открытие страницы регистрации")
    public RegisterPage open() {
        Selenide.open("http://127.0.0.1:9000/register");
        return this;
    }

    @Nonnull
    @Step("Ввод имени пользователя <userName> на странице регистрации")
    public RegisterPage setUserName(String userName) {
        usernameInput.setValue(userName);
        return this;
    }

    @Nonnull
    @Step("Ввод пароля <password> на странице регистрации")
    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Nonnull
    @Step("Повторный ввод пароля <password> для подтверждения на странице регистрации")
    public RegisterPage setPasswordSubmit(String password) {
        submitPasswordInput.setValue(password);
        return this;
    }

    @Nonnull
    @Step("Подтверждение регистрации")
    public RegisterPage submitRegistration() {
        submitRegistrationButton.click();
        return this;
    }

    @Step("Проверка успешной регистрации")
    public void checkSuccessfulRegistration(String text) {
        formParagraphSuccess.shouldBe(text(text));
    }

    @Step("Проверка успешной регистрации")
    public void checkUnsuccessfulRegistrationWithExistUserName(String text) {
        errorMessage.shouldBe(text(text));
    }

    @Step("Проверка успешной регистрации")
    public void checkUnsuccessfulRegistrationIfPasswordAndConfirmPasswordAreNotEqual(String text) {
        errorMessage.shouldBe(text(text));
    }
}
