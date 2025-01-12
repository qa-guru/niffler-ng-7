package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private static final String SUCCESS_REGISTRATION = "Congratulations! You've registered!";
    private static final String PASSWORDS_NOT_EQUAL_ERROR = "Passwords should be equal";

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement submitPasswordInput = $("#passwordSubmit");
    private final SelenideElement signUpButton = $("button[type='submit']");
    private final SelenideElement successRegistrationText = $("p.form__paragraph.form__paragraph_success");
    private final SelenideElement error = $("span.form__error");


    public RegisterPage registerUser(String username, String password, String submitPassword) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitPasswordInput.setValue(submitPassword);
        signUpButton.click();
        return this;
    }

    public void checkRegistrationSuccess() {
        successRegistrationText.shouldHave(text(SUCCESS_REGISTRATION));
    }

    public void checkRegisterExistingUserError(String username) {
        error.shouldHave(text("Username `" + username + "` already exists"));
    }

    public void checkPasswordsNotEqual() {
        error.shouldHave(text(PASSWORDS_NOT_EQUAL_ERROR));
    }
}
