package student.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {

    private final SelenideElement
            usernameInput = $("input[name='username']"),
            passwordInput = $("input[name='password']"),
            passwordSubmitInput = $("input[name='passwordSubmit']"),
            submitButton = $("button[class='form__submit']"),
            errorPasswordsShouldBeEqual = $(byText("Passwords should be equal")),
            errorShortUsername = $(byText("Allowed username length should be from 3 to 50 characters")),
            errorMessage = $(".form__error");
    ;

    public RegisterPage setUserName(String username) {
        usernameInput.shouldBe(visible).setValue(username);
        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordInput.shouldBe(visible).setValue(password);
        return this;
    }

    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmitInput.shouldBe(visible).setValue(password);
        return this;
    }

    public SuccessfulRegisterPage clickSubmitButton() {
        submitButton.shouldBe(visible).click();
        return new SuccessfulRegisterPage();
    }

    public RegisterPage verifyErrorPasswordNotEqualMessage() {
        submitButton.shouldBe(visible).click();
        errorPasswordsShouldBeEqual.shouldBe(visible);
        return this;
    }

    public RegisterPage verifyErrorShortUsernameMessage() {
        submitButton.shouldBe(visible).click();
        errorShortUsername.shouldBe(visible);
        return this;
    }

    public RegisterPage verifyErrorAlreadyExistsUserMessage(String userName) {
        submitButton.shouldBe(visible).click();
        errorMessage.shouldHave(text("Username `%s` already exists".formatted(userName)));
        return this;
    }
}
