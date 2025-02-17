package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Date;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class Calendar {

    private final SelenideElement inputDate =  $("input[name='date']");

    @Step("Выбираем дату в календаре")
    public Calendar selectDateInCalendar(Date date){
        inputDate.setValue(String.valueOf(date));
        return this;
    }
}
