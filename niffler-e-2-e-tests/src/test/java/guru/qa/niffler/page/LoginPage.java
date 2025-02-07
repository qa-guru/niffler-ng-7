package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final static String LOGIN_PAGE_URL = Config.getInstance().authUrl()+"login";

    private final SelenideElement
            usernameInput = $("input[name='username']"),
            passwordInput = $("input[name='password']"),
            submitButton = $("button[type='submit']");

    @Step("Ввод логина <username> и пароля <password>")
    @Nonnull
    public MainPage login(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();

        return new MainPage();
    }

    @Step("Открытие страницы авторизации")
    @Nonnull
    public LoginPage open(){
        Selenide.open(LOGIN_PAGE_URL);
        return this;
    }
}