package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement registerHref = $("a[href='/register']");
  private final SelenideElement errorElement = $(".form__error");

  public MainPage loginSuccess(String username, String password) {
    login(username, password);
    return new MainPage();
  }

  public LoginPage login(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitButton.click();
    return this;
  }

  public RegisterPage goRegister() {
    registerHref.click();
    return new RegisterPage();
  }

  public LoginPage checkError(String message) {
    errorElement.shouldHave(text(message));
    return this;
  }
}
