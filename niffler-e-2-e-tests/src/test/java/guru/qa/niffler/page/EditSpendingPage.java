package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class EditSpendingPage {

    private final SelenideElement
            descriptionInput = $("#description"),
            saveBtn = $("#save");

    public EditSpendingPage setNewSpendingDescription(String description) {
        descriptionInput.clear();
        descriptionInput.setValue(description);
        return this;
    }

    public MainPage save() {
        saveBtn.click();
        return new MainPage();
    }


}
