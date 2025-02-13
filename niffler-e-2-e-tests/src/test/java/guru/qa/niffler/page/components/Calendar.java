package guru.qa.niffler.page.components;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.LocalDate;
import java.time.Month;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class Calendar extends BaseComponent<Calendar> {

    public Calendar(SelenideElement self) {
        super(self);
    }

    public Calendar() {
        super($(".MuiPickersLayout-root"));
    }

    private final SelenideElement
            input = $("input[name='date']"),
            calendarButton = $("button[aria-label*='Choose date']"),
            selectYearButton = self.$("button[aria-label*='calendar view is open']"),
            prevMonthButton = self.$("button[title='Previous month']"),
            nextMonthButton = self.$("button[title='Next month']"),
            currentMonthAndYear = self.$(".MuiPickersCalendarHeader-label");

    private final ElementsCollection
            yearButtons = self.$$("button[class*='MuiPickersYear-yearButton']"),
            dayButtons = self.$$("button[role='gridcell']");

    @Step("Выбор даты в календаре <day>, <month>, <year>")
    public void selectDateInCalendar(LocalDate date) {
        calendarButton.click();
        selectYearButton.click(usingJavaScript());
        yearButtons.find(text(String.valueOf(date.getYear()))).click();
        selectMonth(date.getMonthValue());
        dayButtons.find(text(String.valueOf(date.getDayOfMonth()))).click();
    }

    private void selectMonth(int selectedMonth) {
        int actualMonth = getActualMonthIndex();

        while (actualMonth > selectedMonth) {
            prevMonthButton.click();
            actualMonth = getActualMonthIndex();
        }
        while (actualMonth < selectedMonth) {
            nextMonthButton.click();
            actualMonth = getActualMonthIndex();
        }
    }

    private int getActualMonthIndex() {
        return Month.valueOf(currentMonthAndYear.getText()
                        .split(" ")[0]
                        .toUpperCase())
                .getValue();
    }
}