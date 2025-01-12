package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.User;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.util.GenerateUtils.generatePassword;
import static guru.qa.niffler.util.GenerateUtils.generateUsername;

public class RegistrationTest {
    private static final Config CFG = Config.getInstance();

    @Test
    void shouldRegisterNewUser() {
        User newUser = new User(generateUsername(), generatePassword());

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickToCreateNewUserAccount()
                .registerUser(newUser.getUsername(), newUser.getPassword(), newUser.getPassword())
                .checkRegistrationSuccess();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        User regestedUser = User.getRegestedUser();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickToCreateNewUserAccount()
                .registerUser(regestedUser.getUsername(), regestedUser.getPassword(), regestedUser.getPassword())
                .checkRegisterExistingUserError(regestedUser.getUsername());
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickToCreateNewUserAccount()
                .registerUser(generateUsername(), generatePassword(), generatePassword())
                .checkPasswordsNotEqual();
    }
}
