package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.DataUtils;
import org.junit.jupiter.api.Test;


public class RegisterWebTest {
    private static final Config CFG = Config.getInstance();

    @Test
    void shouldBeRegisterNewUser() {
        String userName = DataUtils.randomUserName();
        String password = DataUtils.randomPassword();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickOnRegisterButton()
                .fillRegisterForm(userName, password, password)
                .checkSuccsessRegister();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        String userName = DataUtils.randomUserName();
        String password = DataUtils.randomPassword();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickOnRegisterButton()
                .fillRegisterForm(userName, password, password)
                .checkSuccsessRegister();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickOnRegisterButton()
                .fillRegisterForm(userName, password, password)
                .checkErrorRegisterMessage(String.format("Username `%s` already exists", userName));
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual(){
        String userName = DataUtils.randomUserName();
        String password = DataUtils.randomPassword();
        String submitPassword = DataUtils.randomPassword();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickOnRegisterButton()
                .fillRegisterForm(userName, password, submitPassword)
                .checkErrorRegisterMessage("Passwords should be equal");
    }
}