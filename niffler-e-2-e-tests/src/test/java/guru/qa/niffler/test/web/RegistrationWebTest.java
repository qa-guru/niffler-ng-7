package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import org.junit.jupiter.api.Test;
import guru.qa.niffler.utils.RandomDataUtils;

import static guru.qa.niffler.page.RegisterPage.register;

@WebTest
public class RegistrationWebTest {

    @Test
    void shouldRegisterNewUser() {
        String password = RandomDataUtils.getRandomPassword();
        register(RandomDataUtils.getRandomUsername(), password, password)
                .shouldSeeSuccessRegistrationText();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        String password = RandomDataUtils.getRandomPassword();
        String username = RandomDataUtils.getRandomUsername();
        register(username, password, password);
        register(username, password, password)
                .shouldSeeUsernameAlreadyExistErrorText(username);
    }


    @Test
    void shouldNShowErrorIfPasswordAndConfirmPasswordAreNotEquals() {
        register(RandomDataUtils.getRandomUsername(),
                RandomDataUtils.getRandomPassword(),
                RandomDataUtils.getRandomPassword())
                .shouldSeePasswordsShouldBeEqualErrorText();
    }


}
