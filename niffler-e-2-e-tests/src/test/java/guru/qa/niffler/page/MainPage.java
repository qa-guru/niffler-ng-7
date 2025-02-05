package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.components.NavigateMenuComponent;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    public final NavigateMenuComponent navigateMenuComponent = new NavigateMenuComponent();
    private final SelenideElement searchField = $("input[placeholder='Search']");

    public EditSpendingPage editSpending(String spendingDescription) {
        searchField.setValue(spendingDescription).pressEnter();
        tableRows.find(text(spendingDescription)).$("td", 5).click();

        return new EditSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        searchField.setValue(spendingDescription).pressEnter();
        tableRows.find(text(spendingDescription)).shouldBe(visible);
    }
}