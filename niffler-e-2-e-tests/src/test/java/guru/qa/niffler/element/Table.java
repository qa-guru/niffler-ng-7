package guru.qa.niffler.element;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;


import java.util.ArrayList;
import java.util.List;

public class Table {

    private final String name;
    private final SelenideElement selector;


    public Table(String name,SelenideElement selector) {
        this.name = name;
        this.selector = selector;
    }

    public ElementsCollection getRows() {
        return selector.$$(By.xpath(".//tr"));
    }

    @NotNull
    private List<ElementsCollection> getRowsWithColumns() {
        ElementsCollection rows = getRows();
        List<ElementsCollection> rowsWithColumns = new ArrayList<>();
        for (SelenideElement row : rows) {
            ElementsCollection rowWithColumns = row.$$(By.xpath(".//td"));
            rowsWithColumns.add(rowWithColumns);
        }
        return rowsWithColumns;
    }

    /**
     * @param rowNumber     - номер строки
     * @param columnsNumber - номер столбца
     * @return - текст из полученого элемента
     */
    public String getValueFromCell(int rowNumber, int columnsNumber) {
        selector.shouldBe(Condition.visible);
        List<ElementsCollection> rowsWithColumns = getRowsWithColumns();
        SelenideElement cell = rowsWithColumns.get(rowNumber - 1).get(columnsNumber - 1);
        return cell.getText();
    }

    @Step("Выбор первой записи из таблицы")
    public SelenideElement getElementFromCell(int row,int column) {
        selector.shouldBe(Condition.visible);
        List<ElementsCollection> rowsWithColumns = getRowsWithColumns();
        return rowsWithColumns.get(row).get(column);
    }

    @Step("Проверяем доступность табилцы")
    public Boolean visibilityCheck(){
        return selector.isDisplayed();
    }
}
