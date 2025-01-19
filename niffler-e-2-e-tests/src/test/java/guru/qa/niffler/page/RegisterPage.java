package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static guru.qa.niffler.actions.FieldActions.setValueField;
import static guru.qa.niffler.page.LoginPage.getLoginPage;
import static java.lang.String.format;
import static java.time.Duration.ofSeconds;

public class RegisterPage {
    private static final ThreadLocal<RegisterPage> INSTANCE = ThreadLocal.withInitial(RegisterPage::new);

    private final SelenideElement usernameInput = $("input[id='username']");
    private final SelenideElement passwordInput = $("input[id='password']");
    private final SelenideElement retryPasswordInput = $("input[id='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement singInButton = $x(".//a[text() = 'Sign in']");

    //Congratulations! You've registered! - текст при подтверждении регистрации

    private RegisterPage() {
    }

    public static RegisterPage getRegisterPage(){
        return INSTANCE.get();
    }

    public RegisterPage setUsername(String username) {
        setValueField(usernameInput, username);
        return this;
    }

    public RegisterPage setPassword(String password) {
        setValueField(passwordInput, password);
        return this;
    }

    public RegisterPage setPasswordSubmit(String password) {
        setValueField(retryPasswordInput, password);
        return this;
    }

    public RegisterPage clickSubmitButton() {
        submitButton.shouldBe(visible).click();
        return this;
    }

    public LoginPage clickSubmitRegistration() {
        singInButton.shouldBe(visible).click();
        return getLoginPage();
    }

    public RegisterPage checkErrorMessagePassword(String message) {
        SelenideElement passwordFieldBlock = passwordInput.parent().$x(".//span[@class='form__error']");
        passwordFieldBlock.shouldHave(text(message).because(format(
                "Вместо текста %s содержится текст %s", message, passwordFieldBlock.getText())));
        return this;
    }

    public RegisterPage checkErrorMessageUsername(String message) {
        SelenideElement passwordFieldBlock = usernameInput.parent().$x(".//span[@class='form__error']");
        passwordFieldBlock.shouldHave(text(message).because(format(
                "Вместо текста %s содержится текст %s", message, passwordFieldBlock.getText())));
        return this;
    }
}