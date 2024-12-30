package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(BrowserExtension.class)
public class LoginWebTest {
    private static final Config CFG = Config.getInstance();

    @Test
    public void mainPageShouldBeDisplayedAfterSuccessLogin(){
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("succesname","succespass")
                .checkCanvas();

    }

    @Test
    public void userShouldStayOnLoginPageAfterLoginWithBadCredentials(){
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("succesname","badpass");
        new LoginPage().checkBadPassWarning();
    }

}
