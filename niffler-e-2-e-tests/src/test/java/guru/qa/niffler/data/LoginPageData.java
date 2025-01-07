package guru.qa.niffler.data;


import guru.qa.niffler.helpers.dataGeneration.NewAccountDataGeneration;
import guru.qa.niffler.helpers.dataGeneration.RandomDataUtils;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
public class LoginPageData {
    protected final String url = "http://127.0.0.1:9000/login";
    protected final String successRegistrationText = "Congratulations! You've registered!";
    protected final String refusalToRegisterText = "Already have an account? Log in!";
    protected final String loginHref = "http://127.0.0.1:3000/main";
    protected final String creatingNewAccountError = "Username `" + new UserData().getLogin() + "` already exists";
    protected final String creatingNewAccountErrorColor = "#F75943";
    protected final String confirmPasswordErrorText = "Passwords should be equal";
    protected final String badCredentialsText = "Неверные учетные данные пользователя";

    @Getter
    @RequiredArgsConstructor
    public enum invalidUserData {
        INVALID_PASSWORD_DATA("Вводим не валидный пароль",new NewAccountDataGeneration().getRandomPassword(),
                new UserData().getLogin()),
        INVALID_USER_NAME_DATA("Вводим не валидный логин",new UserData().getPassword(),RandomDataUtils.getUserName()),
        INVALID_USER_NAME_AND_PASSWORD("Вводим не валидный логин и пароль",
                new NewAccountDataGeneration().getRandomPassword(),RandomDataUtils.getUserName());

        private String name;
        private String passwordValue;
        private String loginValue;

        invalidUserData(String name, String passwordValue, String loginValue) {
            this.name = name;
            this.passwordValue = passwordValue;
            this.loginValue = loginValue;
        }
    }
}
