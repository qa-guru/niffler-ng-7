package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.profileInfo.FriendsPage;
import guru.qa.niffler.page.profileInfo.ProfilePage;
import guru.qa.niffler.page.profileInfo.AllPeoplePage;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {
  private final SelenideElement headerForSpending = $x(".//div[@id='spendings']/h2");
  private final SelenideElement tableSpendings = $("#spendings");
  private final ElementsCollection tableRows = tableSpendings.$$("tbody tr");
  private final SelenideElement headerForDiagram = $x(".//div[@id='stat']/h2");
  private final SelenideElement diagram = $x(".//div[@id='stat']//canvas[@role='img']");

  private final SelenideElement userAvatar = $x(".//button[@aria-label='Menu']");
  private final ElementsCollection linksProfileDropdownMenu = $$x(".//ul[@role='menu']/li//a");

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

  /* Работа с меню при нажатии аватара */
  public MainPage clickByUserAvatar() {
    userAvatar.shouldBe(visible).click();
    return this;
  }

  public ProfilePage clickByProfile() {
    linksProfileDropdownMenu.find(text("Profile")).shouldBe(visible).click();
    return new ProfilePage();
  }

  public FriendsPage clickByFriends() {
    linksProfileDropdownMenu.find(text("Friends")).shouldBe(visible).click();
    return new FriendsPage();
  }

  public AllPeoplePage clickByAllPeople() {
    linksProfileDropdownMenu.find(innerText("All People")).shouldBe(visible).click();
    return new AllPeoplePage();
  }
  /* */
}
