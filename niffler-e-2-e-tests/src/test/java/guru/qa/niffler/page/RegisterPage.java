package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");

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

    public LoginPage submitRegistration() {
        submitButton.click();
        return new LoginPage();
    }

    private LoginPage register(String username, String password, String passwordSubmit) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        passwordSubmitInput.setValue(passwordSubmit);

        submitButton.click();
        return new LoginPage();
    }

    private LoginPage register(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        passwordSubmitInput.setValue(password);

        submitButton.click();
        return new LoginPage();
    }
}
