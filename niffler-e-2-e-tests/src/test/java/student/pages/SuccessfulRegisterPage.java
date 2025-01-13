package student.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class SuccessfulRegisterPage {
    private final SelenideElement
            successRegistrationMessage = $("p[class='form__paragraph form__paragraph_success']"),
            loginButton = $("a[class='form_sign-in']");

    public SuccessfulRegisterPage verifySuccessfulRegistration() {
        successRegistrationMessage.shouldBe(visible);
        return this;
    }

    public LoginPage clickLoginButton() {
        loginButton.shouldBe(visible).click();
        return new LoginPage();
    }
}
