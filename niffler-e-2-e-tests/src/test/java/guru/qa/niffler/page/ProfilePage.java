package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {
    private final SelenideElement photoSvg = $(".MuiContainer-root svg[data-testid='PersonIcon']"); //берем аваторку из основного блока, а не хедера
    private final SelenideElement uploadButton = $("label[for='image__input'] span[role='button']");

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement nameInput = $("input[name='name']");
    private final SelenideElement submitButton = $("button[type='submit']");

    private final SelenideElement showArchivedCheckbox = $("input[type='checkbox']");

    private final SelenideElement categoryInput = $("input[name='category']");

    private final SelenideElement alertMsg = $("div[class='MuiAlert-message']");

    private final ElementsCollection allItemGrid = $$("div.MuiBox-root:has(span.MuiChip-label)"); //строка таблицы: название + кнопки редактирования и архив
    private final ElementsCollection activeItemGrid = $$("div.MuiBox-root:has(div.MuiChip-colorPrimary  span.MuiChip-label)");
    private final ElementsCollection archivedItemGrid = $$("div.MuiBox-root:has(div.MuiChip-colorDefault span.MuiChip-label)");

    private final SelenideElement editCategoryButton = $("button[aria-label='Edit category']");
    private final SelenideElement archiveCategoryButton = $("button[aria-label='Archive category']");
    private final SelenideElement unarchiveCategoryButton = $("button[aria-label='Unarchive category']");

    public ProfilePage setUsername(String username) {
        usernameInput.clear();
        usernameInput.setValue(username);
        return this;
    }

    public ProfilePage setName(String name) {
        nameInput.clear();
        nameInput.setValue(name);
        return this;
    }

    public ProfilePage uploadPhoto(String path) {
        uploadButton.uploadFromClasspath(path);
        return this;
    }

    public ProfilePage submitProfile() {
        submitButton.clear();
        return this;
    }

    public ProfilePage addCategory(String category) {
        categoryInput.setValue(category);
        categoryInput.pressEnter();
        return this;
    }

    public ProfilePage checkCategoryShouldBeActive(String category) {
        activeItemGrid.find(text(category)).should(visible);
        return this;
    }

    public ProfilePage checkCategoryShouldBeArchived(String category) {
        archivedItemGrid.find(text(category)).should(visible);
        return this;
    }
}
