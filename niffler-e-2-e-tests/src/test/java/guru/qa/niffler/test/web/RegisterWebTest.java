package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.Test;

public class RegisterWebTest {
    private static final Config CFG = Config.getInstance();
    public static Faker faker = new Faker();
    String username = faker.pokemon().name();
    String pass = faker.gameOfThrones().dragon();

    @Test
    public void shouldRegisterNewUser(){
        Selenide.open(CFG.registrationUrl(), RegisterPage.class)
                .setUserNameField(username)
                .setPasswordField(pass)
                .setPasswordSubmitField(pass)
                .clickSubmitBtn()
                .clickSingInBtn()
                .login(username,pass)
                .checkCanvas();
    }

    @Test
    public void shouldNotRegisterUserWithExistingUsername(){
        Selenide.open(CFG.registrationUrl(), RegisterPage.class)
                .setUserNameField("succesname")
                .setPasswordField("testpass")
                .setPasswordSubmitField("testpass")
                .clickSubmitBtn()
                .checkExistWarning();
    }

    @Test
    public void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual(){
        Selenide.open(CFG.registrationUrl(), RegisterPage.class)
                .setUserNameField(username)
                .setPasswordField(pass)
                .setPasswordSubmitField(faker.book().title())
                .clickSubmitBtn()
                .checkEqualPassWarning();
    }
}
