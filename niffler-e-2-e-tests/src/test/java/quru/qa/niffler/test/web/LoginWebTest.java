package quru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import quru.qa.niffler.config.Config;
import quru.qa.niffler.helper.UserDataHelper;
import quru.qa.niffler.model.UserJson;
import quru.qa.niffler.page.LoginPage;

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
        UserJson randomUser = UserDataHelper.getRandomUser();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(randomUser.username(),
                        randomUser.password());
        new LoginPage().shouldSeeErrorWithBadCredentialsText();
    }


}
