package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import guru.qa.niffler.page.components.NavigateMenuComponent;
import guru.qa.niffler.page.components.SearchField;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final SearchField searchField = new SearchField();

    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    public final NavigateMenuComponent navigateMenuComponent = new NavigateMenuComponent();

    public EditSpendingPage editSpending(String spendingDescription) {
        searchField.search(spendingDescription);
        tableRows.find(text(spendingDescription)).$("td", 5).click();

        return new EditSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        searchField.search(spendingDescription);
        tableRows.find(text(spendingDescription)).shouldBe(visible);
    }
}