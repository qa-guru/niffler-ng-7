package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {
    //Toolbar
    private final SelenideElement profileBtn = $("button div.MuiAvatar-root");
    //Profile
    private final SelenideElement profileHeader = $(By.xpath("//h2[text()='Profile']"));
    private final SelenideElement profileImg = $("div.MuiContainer-root img]");
    private final SelenideElement uploadImgBtn =$("span[role='button']");
    private final SelenideElement usernameField = $("input#username");
    private final SelenideElement nameField = $("input#name");
    private final SelenideElement saveChangesBtn = $("button[type='submit']");
    //Categories
    private final SelenideElement categoriesHeader = $(By.xpath("//h2[text()='Categories']"));
    private final SelenideElement showArchivedToggle = $("input[type='checkbox']");
    private final SelenideElement newCategoryInput = $("input#category");
    private final ElementsCollection activeCategoryRows =
            $$(By.xpath("//div[contains(@class, 'MuiGrid-item')][.//button[@aria-label = 'Archive category']]"));
    private final ElementsCollection archivedCategoryRows =
            $$(By.xpath("//div[contains(@class, 'MuiGrid-item')][.//button[@aria-label = 'Unarchive category']]"));
    private final By archiveBtn = By.cssSelector("button[aria-label='Archive category']");
    private final By unArchiveBtn = By.cssSelector("button[aria-label='Unarchive category']");
    //Archive category pop up
    private final SelenideElement popUpArchiveBtn = $(By.xpath("//button[text() = 'Archive']"));
    private final SelenideElement popUpUnArchiveBtn = $(By.xpath("//button[text() = 'Unarchive']"));

    public ProfilePage profilePageIsOpened () {
        profileHeader.shouldBe(visible);
        categoriesHeader.shouldBe(visible);
        return this;
    }

    public ProfilePage checkThatArchivedCategoryIsPresented(String name) {
        archivedCategoryRows.find(text(name)).shouldBe(visible);
        return this;
    }

    public ProfilePage checkThatActiveCategoryIsPresented(String name) {
        activeCategoryRows.find(text(name)).shouldBe(visible);
        return this;
    }

    public ProfilePage archiveCategory(String name) {
        activeCategoryRows
                .find(text(name))
                .shouldBe(visible)
                .findElement(archiveBtn)
                .click();
        popUpArchiveBtn.shouldBe(visible).click();
        return this;
    }

    public ProfilePage showArchivedCategories() {
        showArchivedToggle.click();
        return this;
    }

    public ProfilePage unArchiveCategory(String name) {
        archivedCategoryRows
                .find(text(name))
                .shouldBe(visible)
                .findElement(unArchiveBtn)
                .click();
        popUpUnArchiveBtn.shouldBe(visible).click();
        return this;
    }
}
