package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement loginHeader = $(By.xpath("//h1[text() = 'Log in']"));
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitBtn = $("button[type='submit']");
    private final SelenideElement createAccountBtn = $("a.form__register");
    private final SelenideElement errorMsg = $("p.form__error");

    public MainPage doLogin(String username, String password)  {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitBtn.click();
        return new MainPage();
    }

    public LoginPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public LoginPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public LoginPage submitCredentials() {
        submitBtn.click();
        return this;
    }

    public RegisterPage openSignUpPage() {
        createAccountBtn.click();
        return new RegisterPage();
    }

    public void checkThatUserIsOnLoginPage() {
        loginHeader.should(visible);
    }

    public void errorMessageShouldContainText(String messageText) {
        errorMsg.shouldHave(text(messageText));
    }

}
