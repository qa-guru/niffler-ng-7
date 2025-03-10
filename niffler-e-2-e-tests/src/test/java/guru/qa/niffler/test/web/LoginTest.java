package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.SelenideUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;


public class LoginTest {


    @RegisterExtension
    private final BrowserExtension browserExtension = new BrowserExtension();
    SelenideDriver driver = new SelenideDriver(SelenideUtils.chromeConfig);

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
        SelenideDriver firefox = new SelenideDriver(SelenideUtils.firefoxConfig);

        browserExtension.drivers().addAll(List.of(driver, firefox))
        ;
        String userName = "taty";
        String badPassword = "12345";
        driver.open(CFG.frontUrl());
        new LoginPage(driver)
                .setUserName(userName)
                .setPassword(badPassword)
                .checkBadCredentialError();
    }
}