package guru.qa.niffler.page.components;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.ParametersAreNonnullByDefault;
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
            yearButtons = self.$$("button[class='MuiPickersYear-yearButton']"),
            dayButtons = self.$$("button[role='gridcell']");

    @Step("Выбор даты в календаре <day>, <month>, <year>")
    public void selectDateInCalendar(int day, int month, int year) {
        calendarButton.click();
        selectYearButton.click(usingJavaScript());
        yearButtons.find(text(String.valueOf(year))).click();
        selectMonth(month);
        dayButtons.find(text(String.valueOf(day))).click();
    }

    private void selectMonth(int selectedMonth) {
        int actualMonth = getActualMonthIndex();

        while (actualMonth > selectedMonth) {
            prevMonthButton.click();
            Selenide.sleep(200);
            actualMonth = getActualMonthIndex();
        }
        while (actualMonth < selectedMonth) {
            nextMonthButton.click();
            Selenide.sleep(200);
            actualMonth = getActualMonthIndex();
        }
    }

    private int getActualMonthIndex() {
        return Month.valueOf(currentMonthAndYear.getText()
                        .split(" ")[0]
                        .toUpperCase())
                .ordinal();
    }
}