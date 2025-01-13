package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static guru.qa.niffler.page.ProfilePage.getProfilePage;

public class MainPage {
  private final SelenideElement headerForSpending = $x(".//div[@id='spendings']/h2");
  private final SelenideElement tableSpendings = $("#spendings");
  private final ElementsCollection tableRows = tableSpendings.$$("tbody tr");
  private final SelenideElement headerForDiagram = $x(".//div[@id='stat']/h2");
  private final SelenideElement diagram = $x(".//div[@id='stat']//canvas[@role='img']");

  private final SelenideElement userAvatar = $x(".//button[@aria-label='Menu']");
  private final ElementsCollection linksProfileDropdownMenu = $$x(".//ul[@role='menu']/li");

  public EditSpendingPage editSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).$$("td").get(5).shouldBe(visible).click();
    return new EditSpendingPage();
  }

  public void checkThatTableContainsSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).shouldBe(visible);
  }

  public MainPage checkDiagramStatistics() {
    diagram.shouldBe(visible);
    return this;
  }

  public MainPage checkTableSpending() {
    diagram.shouldBe(visible);
    return this;
  }

  public MainPage clickByUserAvatar() {
    userAvatar.shouldBe(visible).click();
    return this;
  }

  public ProfilePage clickByProfile() {
    linksProfileDropdownMenu.find(text("Profile")).shouldBe(visible).click();
    return getProfilePage();
  }
}
