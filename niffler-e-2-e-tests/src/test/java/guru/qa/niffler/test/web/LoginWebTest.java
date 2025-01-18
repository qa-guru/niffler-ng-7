package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.utils.RandomDataUtils;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;

@WebTest
public class LoginWebTest {

    private static final Config CFG = Config.getInstance();


    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("test", "test")
                .checkThatMainPageVisible();
    }


    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(RandomDataUtils.getRandomUsername(),
                        RandomDataUtils.getRandomPassword());
        new LoginPage().shouldSeeErrorWithBadCredentialsText();
    }


}