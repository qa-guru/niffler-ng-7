package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    public static final String BAD_CREDENTIALS_ERROR = "Неверные учетные данные пользователя";

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement createNewAccountButton = $("a[href='/register']");
    private final SelenideElement credentialsError = $(".form__error");

    public MainPage doLogin(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
        return new MainPage();
    }

    public LoginPage doLoginWithBadCredentials(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
        return this;
    }

    public RegisterPage clickToCreateNewUserAccount() {
        createNewAccountButton.click();
        return new RegisterPage();
    }

    public void checkCredentialsError() {
        credentialsError.shouldHave(text(BAD_CREDENTIALS_ERROR));
    }
}
