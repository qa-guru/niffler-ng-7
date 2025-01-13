package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
  public HeaderComponent header = new HeaderComponent();

  private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
  private final SelenideElement statisticsElement = $("#stat");
  private final SelenideElement spendingElement = $("#spendings");

  public EditSpendingPage editSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  public void checkThatTableContainsSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).should(visible);
  }

  public MainPage checkThatHaveStatistics() {
    statisticsElement.should(visible);
    return this;
  }

  public MainPage checkThatHaveSpendings() {
    spendingElement.should(visible);
    return this;
  }

  public MainPage checkLoadPage() {
    checkThatHaveStatistics();
    checkThatHaveSpendings();
    return this;
  }
}
