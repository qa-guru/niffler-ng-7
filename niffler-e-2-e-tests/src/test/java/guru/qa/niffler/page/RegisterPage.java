package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.interactable;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement
            userNameInput = $("#username"),
            passwordInput = $("#password"),
            passwordSubmitInput = $("#passwordSubmit"),
            successRegisterText = $(".form__paragraph_success"),
            errorRegisterText = $(".form__error");


    public RegisterPage fillRegisterForm(String userName, String password, String submitPassword) {
        setUserName(userName);
        setPassword(password);
        setPasswordSubmit(submitPassword);
        return this;
    }

    public RegisterPage setUserName(String userName) {
        userNameInput.shouldBe(interactable).setValue(userName);
        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmitInput.setValue(password).pressEnter();
        return this;
    }

    public RegisterPage checkSuccsessRegister() {
        successRegisterText.shouldHave(text("Congratulations! You've registered!"));
        return this;
    }

    public RegisterPage checkErrorRegisterMessage(String text) {
        errorRegisterText.shouldHave(text(text));
        return this;
    }
}