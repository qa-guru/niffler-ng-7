package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.condition.SpendConditions;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.EditSpendingPage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.List;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class SpendingTable extends BaseComponent<SpendingTable>{

    public SpendingTable() {
        super($("#spendings"));
    }


    private final SearchField searchField = new SearchField();
    private final SelenideElement periodMenu = self.$("#period"),
            deleteBtn = $("#delete"),
            popup = $("div[role='dialog']"),
            tableHeader = self.$(".MuiTableHead-root");

    private final ElementsCollection menuItems =
            $$(".MuiList-padding li"),
            headerCells = tableHeader.$$(".MuiTableCell-root"),
            tableRows = self.$$("tr");




    @Step("Выбираем период")
    @Nonnull
    public SpendingTable selectPeriod(DataFilterValues period) {
        periodMenu.click();
        menuItems.find(text(period.text)).click();
        return this;
    }


    @Step("Изменяем трату")
    @Nonnull
    public EditSpendingPage editSpending(String description) {
        searchSpendingByDescription(description);
        SelenideElement row = tableRows.find(text(description));
        row.$$("td").get(5).click();
        return new EditSpendingPage();
    }

    @Step("Удаляем трату")
    @Nonnull
    public SpendingTable deleteSpending(String description) {
        searchSpendingByDescription(description);
        SelenideElement row = tableRows.find(text(description));
        row.$$("td").get(0).click();
        deleteBtn.click();
        popup.$(byText("Delete")).click(usingJavaScript());
        return this;
    }

    @Step("Проверяем наличие трат")
    @Nonnull
    public SpendingTable searchSpendingByDescription(String description) {
        searchField.search(description);
        return this;
    }

    @Step("Проверяем наличие трат: '{0}'")
    @Nonnull
    public SpendingTable checkTableContains(String... expectedSpends) {
        searchSpendingByDescription(expectedSpends[0]);
        tableRows.find(text(expectedSpends[0])).should(visible);
        return this;
    }

    @Step("Проверяем, что в таблице' {0}' записей")
    @Nonnull
    public SpendingTable checkTableSize(int expectedSize) {
        tableRows.should(size(expectedSize));
        return this;
    }

    public SpendingTable checkSpendTable(List<SpendJson> spendJsons) {
        tableRows.should(SpendConditions.expectedSpends(spendJsons));
        return this;
    }
}
