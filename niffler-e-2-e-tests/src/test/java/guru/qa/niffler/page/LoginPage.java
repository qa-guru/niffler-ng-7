package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.element.Button;
import guru.qa.niffler.element.Input;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    protected final Input loginInput = new Input("Input Login", $(By.name("username")));
    protected final Input passwordInput = new Input("Input Password", $(By.name("password")));
    private final Button loginButton = new Button("Кнопка Log In",
            $(".form__submit"));

    @Step("Открываем страницу авторизации")
    public LoginPage openAuthorizationPage(String url) {
        Selenide.open(url);
        return this;
    }

    @Step("Вводим значение в Input {name}")
    public LoginPage setAuthorizationInput(String value, @NotNull String name) {
        switch (name) {
            case "login":
                loginInput.setValue(value);
                break;
            case "password":
                passwordInput.setValue(value);
                break;
            default:
                throw new RuntimeException("Указанного Input нет на странице");
        }
        return this;
    }

    @Step("Нажимаем кнопку Log In")
    public LoginPage loginButtonClick() {
        loginButton.buttonClick();
        return this;
    }
}
