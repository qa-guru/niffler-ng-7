package quru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterPage {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement successText = $(".form__paragraph_success");
    private final SelenideElement errorText = $(".form__error");

    public RegisterPage setUsername(String username) {
        usernameInput.clear();
        usernameInput.setValue(username);
        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordInput.clear();
        passwordInput.setValue(password);
        return this;
    }

    public RegisterPage setPasswordSubmit(String passwordSubmit) {
        passwordSubmitInput.clear();
        passwordSubmitInput.setValue(passwordSubmit);
        return this;
    }

    public RegisterPage submitRegistration() {
        submitButton.click();
        return this;
    }

    public void shouldSeeSuccessRegistrationText() {
        successText.should(visible);
    }

    public void shouldSeeUsernameAlreadyExistErrorText(String username) {
        errorText.should(visible);
        String expected = "Username `" + username + "` already exists";
        assertEquals(expected, errorText.getText());
    }

    public void shouldSeePasswordsShouldBeEqualErrorText() {
        errorText.should(visible);
        String expected = "Passwords should be equal";
        assertEquals(expected, errorText.getText());
    }
}
