package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Calendar;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Date;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class EditSpendingPage extends BasePage<EditSpendingPage>{

    private final Calendar calendar = new Calendar();

    private final SelenideElement
            amountInput = $("#amount"),
            categoryInput = $("#category"),
            descriptionInput = $("#description"),
            saveBtn = $("#save");


    @Step("Устанавливаем новое описание: '{0}'")
    @Nonnull
    public EditSpendingPage setDescription(String description) {
        descriptionInput.clear();
        descriptionInput.setValue(description);
        return this;
    }

    @Step("Добавляем новую категорию")
    @Nonnull
    public EditSpendingPage setCategory(String category) {
        categoryInput.clear();
        categoryInput.setValue(category);
        return this;
    }

    @Step("Устанавливаем сумму '{0}'")
    @Nonnull
    public EditSpendingPage setSpendingAmount(double amount) {
        amountInput.shouldBe(clickable);
        amountInput.clear();
        amountInput.setValue(String.valueOf(amount));
        return this;
    }

    @Step("Устанавливаем дату '{0}'")
    @Nonnull
    public EditSpendingPage setSpendingDate(Date date) {
        calendar.selectDateInCalendar(date);
        return this;
    }

    @Step("Cохраняем изменения")
    @Nonnull
    public EditSpendingPage save() {
        saveBtn.click();
        return this;
    }
}
