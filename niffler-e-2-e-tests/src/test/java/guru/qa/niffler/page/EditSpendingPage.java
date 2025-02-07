package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.components.Calendar;
import guru.qa.niffler.page.components.Header;
import io.qameta.allure.Step;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@Getter
@ParametersAreNonnullByDefault
public class EditSpendingPage {

    private final Header header = new Header();
    private final Calendar calendar = new Calendar();

    private final SelenideElement
            descriptionInput = $("#description"),
            saveButton = $("#save"),
            amountInput = $("#amount"),
            categoryInput = $("#category"),
            cancelButton = $("#cancel");

    @Step("Изменение названия траты <description>")
    @Nonnull
    public EditSpendingPage editDescription(String description) {
        descriptionInput.clear();
        descriptionInput.setValue(description);
        return this;
    }

    @Step("Сохранение изменений")
    @Nonnull
    public MainPage saveChange() {
        saveButton.click();
        return new MainPage();
    }


}