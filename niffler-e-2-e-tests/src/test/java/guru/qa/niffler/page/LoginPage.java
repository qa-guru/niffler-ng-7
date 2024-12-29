package guru.qa.niffler.page;


import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.interactable;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement
            userNameInput = $("input[name='username']"),
            passwordInput = $("input[name='password']"),
            formRegisterButton = $(".form__register"),
            errorForm = $(".form__error");

    public RegisterPage clickOnRegisterButton() {
        formRegisterButton.click();
        return new RegisterPage();
    }

    public MainPage doLogin(String userName, String password) {
        setUserName(userName);
        setPassword(password);
        return new MainPage();
    }

    public LoginPage setUserName(String userName) {
        userNameInput.shouldBe(interactable).setValue(userName);
        return this;
    }

    public LoginPage setPassword(String userName) {
        passwordInput.setValue(userName).pressEnter();
        return this;
    }

    public LoginPage checkBadCredentialError() {
        errorForm.shouldHave(text("Неверные учетные данные пользователя"));
        return this;
    }
}