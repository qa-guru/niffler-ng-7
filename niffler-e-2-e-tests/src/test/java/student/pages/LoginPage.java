package student.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement
            usernameInput = $("input[name='username']"),
            passwordInput = $("input[name='password']"),
            submitButton = $("button[type='submit']"),
            registerButton = $("a[class='form__register']"),
            errorLoginMessage = $("p[class='form__error']"),
            logInTitle = $("h1[class='header']");


    public LoginPage setUserName(String username) {
        usernameInput.shouldBe(visible).setValue(username);
        return this;
    }

    public LoginPage setPassword(String password) {
        passwordInput.shouldBe(visible).setValue(password);
        return this;
    }

    public MainPage clickSubmitButton() {
        submitButton.shouldBe(visible).click();
        return new MainPage();
    }

    public MainPage enterWithLoginAndPassword(String username, String password) {
        usernameInput.shouldBe(visible).setValue(username);
        passwordInput.shouldBe(visible).setValue(password);
        submitButton.shouldBe(visible).click();
        return new MainPage();
    }

    public RegisterPage clickRegisterButton() {
        registerButton.shouldBe(visible).click();
        return new RegisterPage();
    }

    public LoginPage verifyErrorCredentialsMessage() {
        submitButton.shouldBe(visible).click();
        errorLoginMessage.shouldBe(visible).shouldHave(text("Неверные учетные данные пользователя"));
        return this;
    }

    public LoginPage verifyLoginPage() {
        logInTitle.shouldBe(visible).shouldHave(text("Log in"));
        return this;
    }
}
