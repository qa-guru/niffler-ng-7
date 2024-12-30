package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {
    private final SelenideElement

            categoryInput = $("input#category"),
            showArchivedCheckbox = $("input[type='checkbox']"),
            archiveButton = $("button[aria-label = 'Archive category']"),
            unarchiveButton = $("button[aria-label = 'Unarchive category']");

    private final ElementsCollection
            categories = $$(".css-17u3xlq");

    public ProfilePage setNewCategory(String newCategory) {
        categoryInput.shouldBe(visible).setValue(newCategory).submit();
        return this;
    }

    public ProfilePage clickArchivedCheckbox() {
        showArchivedCheckbox.click();
        return this;
    }

    public void checkCategoryInCategoryList(String categoryName) {
        categories.findBy(text(categoryName)).shouldBe(visible);
    }

    public void pickArchiveCategory() {
        archiveButton.shouldBe(visible).click();
    }
}
