package guru.qa.niffler.tests.web;

import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
@DisplayName("Web тесты на регистрацию")
public class RegistrationWebTests {

    @DisplayName("Тест на успешную регистрацию нового пользователя")
    @Test
    void shouldRegisterNewUser() {
        new RegisterPage()
                .open()
                .setUserName("Artur3")
                .setPassword("12345")
                .setPasswordSubmit("12345")
                .submitRegistration()
                .checkSuccessfulRegistration("Congratulations! You've registered!");
    }
}
