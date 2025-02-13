package guru.qa.niffler.tests.web;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@WebTest
@DisplayName("Web тесты на регистрацию")
public class LoginWebTests {

    @DisplayName("Отображение главной страницы после успешной авторизации")
    @User(
            categories = {
                    @Category(name = "Магазины", archived = false),
                    @Category(name = "Бары", archived = true)
            },
            spendings = {
                    @Spending(
                            category = "Обучение",
                            description = "QA.GURU Advanced 7",
                            amount = 80000
                    )
            }
    )
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin(UserJson user) {
        new LoginPage()
                .open()
                .login(user.username(), user.testData().password());
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
                .login("Artur", "54321");
        $(".header").shouldBe(visible)
                .shouldHave(text("Log in"));
        $(".form__error").shouldBe(visible)
                .shouldHave(text("Неверные учетные данные пользователя"));
    }

}
