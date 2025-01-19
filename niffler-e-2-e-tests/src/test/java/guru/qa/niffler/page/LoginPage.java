package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static guru.qa.niffler.actions.FieldActions.setFieldAndCheck;
import static guru.qa.niffler.page.RegisterPage.getRegisterPage;
import static java.lang.String.format;

public class LoginPage {
  private final static ThreadLocal<LoginPage> INSTANCE = ThreadLocal.withInitial(LoginPage::new);

  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement registerButton = $x(".//a[contains(@class, 'form__register')]");
  private final SelenideElement errorMessage = $x(".//*[contains(@class, 'form__error-container')]");


  public LoginPage setUsername(String username) {
    setFieldAndCheck(usernameInput, username);
    return this;
  }

  public LoginPage setPassword(String password) {
    setFieldAndCheck(passwordInput, password);
    return this;
  }

  public RegisterPage clickRegisterButton() {
    registerButton.shouldBe(visible).click();
    return getRegisterPage();
  }

  public LoginPage checkErrorMessage(String message) {
    errorMessage.shouldHave(text(message).because(format(
            "Вместо текста %s содержится текст %s", message, errorMessage.getText())));
    return this;
  }

  public MainPage login(String username, String password) {
    setUsername(username)
            .setPassword(password);
    submitButton.shouldBe(visible).click();
    return new MainPage();
  }

  private LoginPage() {
  }

  public static LoginPage getLoginPage(){
    return INSTANCE.get();
  }
}