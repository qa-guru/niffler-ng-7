package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement pageName = $("h1.header");
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitPasswordInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitBtn = $("button[type='submit']");
    private final SelenideElement loginBtn = $("a.form_sign-in");
    private final SelenideElement successMsg = $("p.form__paragraph_success");
    private final SelenideElement errorMsg = $("span.form__error");

    public RegisterPage setUserName(String userName) {
        pageName.shouldHave(text("Sign up"));
        usernameInput.setValue(userName);
        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public RegisterPage setPasswordSubmit(String passwordSubmit) {
        submitPasswordInput.setValue(passwordSubmit);
        return this;
    }

    public RegisterPage submitRegistration() {
        submitBtn.click();
        return this;
    }

    public RegisterPage checkThatSuccessMsgContains(String messageText) {
        successMsg.shouldHave(text(messageText));
        return this;
    }

    public LoginPage openLoginPage() {
        loginBtn.click();
        return new LoginPage();
    }

    public void checkThatErrorMessageContainsText(String msgText) {
        errorMsg.shouldHave(text(msgText));
    }
}
