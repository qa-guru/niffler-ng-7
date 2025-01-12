package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement errorElement = $(".form__error");

    private final SelenideElement formSignInHref = $("a.form_sign-in");

    public RegisterPage setUsernameInput(String username) {
        usernameInput.clear();
        usernameInput.setValue(username);
        return this;
    }

    public RegisterPage setPasswordInput(String password) {
        passwordInput.clear();
        passwordInput.setValue(password);
        return this;
    }

    public RegisterPage setPasswordSubmitInput(String password) {
        passwordSubmitInput.clear();
        passwordSubmitInput.setValue(password);
        return this;
    }

    public LoginPage submitRegistrationSuccess() {
        submitRegistration();
        formSignInHref.should(visible);
        formSignInHref.click();
        return new LoginPage();
    }

    public RegisterPage submitRegistration() {
        submitButton.click();
        return this;
    }

    public LoginPage register(String username, String password, String passwordSubmit) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        passwordSubmitInput.setValue(passwordSubmit);

        submitRegistrationSuccess();
        return new LoginPage();
    }

    public LoginPage register(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        passwordSubmitInput.setValue(password);

        submitRegistrationSuccess();
        return new LoginPage();
    }

    public RegisterPage checkErrorMessage(String message) {
        errorElement.shouldHave(text(message));
        return this;
    }
}
