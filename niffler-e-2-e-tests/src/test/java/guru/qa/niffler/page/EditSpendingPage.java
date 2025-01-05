package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class EditSpendingPage {

  private final SelenideElement descriptionInput = $("#description");
  private final SelenideElement saveBtn = $("#save");

    public void setDescription(String description) {
        descriptionInput.clear();
        descriptionInput.setValue(description);
        saveBtn.click();
    }
}
