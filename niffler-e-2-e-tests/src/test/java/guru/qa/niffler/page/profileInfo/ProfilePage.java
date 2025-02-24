package guru.qa.niffler.page.profileInfo;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage {
    private final SelenideElement archiveButton = $x(".//button[@aria-label = 'Archive category']");
    private final SelenideElement showArchiveCategories = $x(
            ".//*[./*[normalize-space(text()) = 'Show archived']]");
    private final ElementsCollection archivedCategoriesName = $$x(
            ".//*[./button[contains(@aria-label, 'category')]]/preceding-sibling::*");

    public ProfilePage activatedShowArchiveCategory() {
        showArchiveCategories.shouldBe(visible).click();
        return this;
    }

    public ProfilePage checkNameSpendInCategoryList(String name) {
        archivedCategoriesName.find(text(name)).shouldBe(visible);
        return this;
    }

    public ProfilePage() {
    }
}
