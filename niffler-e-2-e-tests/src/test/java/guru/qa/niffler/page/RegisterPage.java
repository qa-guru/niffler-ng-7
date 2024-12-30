package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement nifflerLogo = $(".logo-section");
    private final SelenideElement header = $(By.xpath("//*[contains(text(), 'Sign up')]"));

    private final SelenideElement loginSuggestion = $(By.xpath("//*[contains(text(), 'Log in')]"));
    public RegisterPage clickLoginSuggestion(){
        loginSuggestion.click();
        return new RegisterPage();
    }

    private final SelenideElement userNameField = $(By.xpath("//*[@id='username']"));

    public RegisterPage setUserNameField(String userName){
        userNameField.clear();
        userNameField.setValue(userName);
        return new RegisterPage();
    }

    private final SelenideElement passwordField = $(By.xpath("//*[@id='password']"));
    public RegisterPage setPasswordField(String passwordValue){
        passwordField.clear();
        passwordField.setValue(passwordValue);
        return new RegisterPage();
    }

    private final SelenideElement passwordSubmitField = $(By.xpath("//*[@id='passwordSubmit']"));
    public RegisterPage setPasswordSubmitField(String passwordSubmitValue){
        passwordSubmitField.clear();
        passwordSubmitField.setValue(passwordSubmitValue);
        return new RegisterPage();
    }

    private final SelenideElement passwordBtn = $(By.xpath("//*[@id='passwordBtn'"));
    public RegisterPage clickPasswordBtn(){
        passwordBtn.click();
        return new RegisterPage();
    }

    private final SelenideElement passwordSubmitBtn = $(By.xpath("//*[@id='passwordSubmitBtn']"));
    public RegisterPage clickPasswordSubmitBtn(){
        passwordSubmitBtn.click();
        return new RegisterPage();
    }

    private final SelenideElement submitBtn = $(By.xpath("//*[@type='submit']"));
    public RegisterPage clickSubmitBtn(){
        submitBtn.click();
        return new RegisterPage();
    }

    private final SelenideElement signInBtn = $(By.xpath("//*[@class='form_sign-in']"));
    public LoginPage clickSingInBtn(){
        signInBtn.click();
        return new LoginPage();
    }

    private final SelenideElement existWarning = $(By.xpath("//span[contains(text(), 'already exists')]"));
    public void checkExistWarning(){
        existWarning.shouldBe(visible);
    }

    private final SelenideElement equalPassWarning = $(By.xpath("//span[contains(text(), 'Passwords should be equal')]"));
    public void checkEqualPassWarning(){
        equalPassWarning.shouldBe(visible);
    }
}
