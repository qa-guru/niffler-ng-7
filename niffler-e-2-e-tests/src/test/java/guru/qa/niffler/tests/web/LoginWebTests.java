package guru.qa.niffler.tests.web;

import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;


@ExtendWith(BrowserExtension.class)
@DisplayName("Web тесты на регистрацию")
public class LoginWebTests {

    static final String userName = "Artur";
    static final String password = "12345";

    @DisplayName("Отображение главной страницы после успешной авторизации")
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        new LoginPage()
                .open()
                .login(userName, password);
        $(".css-giaux5").shouldBe(visible)
                .shouldHave(text("Statistics"));
        $(".css-uxhuts").shouldBe(visible)
                .shouldHave(text("History of Spendings"));
    }

    @DisplayName("Пользователь должен остаться на странице авторизации после авторизации с некорректными кредами")
    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        new LoginPage()
                .open()
                .login(userName, "54321");
        $(".header").shouldBe(visible)
                .shouldHave(text("Log in"));
        $(".form__error").shouldBe(visible)
                .shouldHave(text("Неверные учетные данные пользователя"));
    }

}
