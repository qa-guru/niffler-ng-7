package guru.qa.niffler.page.component;

import io.qameta.allure.Step;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Date;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class Calendar extends BaseComponent<Calendar> {

    public Calendar() {
        super($("input[name='date']"));
    }

    @Step("Выбираем дату в календаре")
    public Calendar selectDateInCalendar(Date date) {
        self.setValue(String.valueOf(date));
        return this;
    }
}
