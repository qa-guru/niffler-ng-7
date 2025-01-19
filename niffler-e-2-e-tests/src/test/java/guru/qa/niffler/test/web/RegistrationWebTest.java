package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.config.Constants;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.page.LoginPage.getLoginPage;
import static guru.qa.niffler.util.DataGenerator.getRandomPassword;
import static guru.qa.niffler.util.DataGenerator.getRandomUsername;
import static java.lang.String.format;


@ExtendWith(BrowserExtension.class)
public class RegistrationWebTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void shouldRegisterNewUser() {
        String username = getRandomUsername();
        String password = getRandomPassword();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(password)
                .clickSubmitButton()
                .clickSubmitRegistration()
                .login(username, password)
                .checkDiagramStatistics();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUsername(Constants.userName)
                .setPassword(Constants.password)
                .setPasswordSubmit(Constants.password)
                .clickSubmitButton()
                .checkErrorMessageUsername(format("Username `%s` already exists", Constants.userName));
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUsername(getRandomUsername())
                .setPassword(getRandomPassword())
                .setPasswordSubmit(getRandomPassword())
                .clickSubmitButton()
                .checkErrorMessagePassword("Passwords should be equal");
    }

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(Constants.userName, Constants.password)
                .checkDiagramStatistics()
                .checkTableSpending();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(getRandomUsername(), getRandomPassword());
        getLoginPage().checkErrorMessage("Неверные учетные данные пользователя");
    }
}
