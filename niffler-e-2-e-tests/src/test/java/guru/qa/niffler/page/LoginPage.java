package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement registerLink = $(".form__register");
    private final SelenideElement errorText = $(".form__error-container");

    public MainPage login(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
        return new MainPage();
    }

    public RegisterPage openRegistrationPage() {
        registerLink.click();
        return new RegisterPage();
    }

    public void shouldSeeErrorWithBadCredentialsText() {
        errorText.should(visible);
//        String expected = "Bad credentials";
        String expected = "Неверные учетные данные пользователя";
        errorText.shouldHave(text(expected));
    }
}
