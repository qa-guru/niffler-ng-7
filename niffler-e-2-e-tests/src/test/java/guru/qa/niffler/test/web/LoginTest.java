package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.converter.Browser;
import guru.qa.niffler.jupiter.converter.BrowserConverter;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.NonStaticBrowsersExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;


@WebTest
public class LoginTest {


    private final BrowserExtension browserExtension = new BrowserExtension();

    @RegisterExtension
    private final NonStaticBrowsersExtension nonStaticBrowsersExtension = new NonStaticBrowsersExtension();
    SelenideDriver driver = new SelenideDriver(Browser.CHROME.getConfig());

    private static final Config CFG = Config.getInstance();


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
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .checkIsLoaded();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        SelenideDriver firefox = new SelenideDriver(Browser.FIREFOX.getConfig());
        nonStaticBrowsersExtension.drivers().addAll(List.of(driver, firefox));
        String userName = "taty";
        String badPassword = "12345";
        driver.open(CFG.frontUrl());
        new LoginPage(driver)
                .setUserName(userName)
                .setPassword(badPassword)
                .checkBadCredentialError();
    }

    @ParameterizedTest
    @EnumSource(Browser.class)
    void browserParameterizedTest(@ConvertWith(BrowserConverter.class) SelenideDriver driver) {
        final String username = randomUsername();
        final String incorrectPassword = "12345";

        driver.open(LoginPage.URL);
        new LoginPage(driver)
                .setUserName(username)
                .setPassword(incorrectPassword)
                .checkBadCredentialError();
    }
}