package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

@ParametersAreNonnullByDefault
public class UserProfilePage extends BasePage<UserProfilePage>{

    public static String URL = Config.getInstance().frontUrl() + "profile";

    private final SelenideElement
            dialogModal = $x("//div[@role='dialog']"),
            confirmButton = $x("//button[contains(text(),'chive')]"),
            switchToggle = $(".MuiSwitch-switchBase"),
            nameInput = $("#name"),
            userName = $("#username"),
            uploadPickButton = $x("//label[@for='image__input']");

    private final ElementsCollection bubblesArchived = $$(".MuiChip-filled.MuiChip-colorDefault");


    @Override
    @Step("Check that page is loaded")
    @Nonnull
    public UserProfilePage checkThatPageLoaded() {
        userName.should(visible);
        return this;
    }

    @Step("Меняем состояние категории '{0}'")
    @Nonnull
    public UserProfilePage changeArchiveCategoryStateByName(String category, String state) {
        SelenideElement archiveButton = $x(String.format("//span[text()='%s']/../..//button[@aria-label='%s category']", category, state));
        archiveButton.shouldBe(clickable).click();
        return this;
    }

    @Step("Подтверждение изменеия состояния категории")
    @Nonnull
    public UserProfilePage confirmArchiveCategory() {
        dialogModal.shouldBe(visible);
        confirmButton.click();
        $(".MuiAlert-message").shouldBe(visible);
        return this;
    }

    @Step("Переключаем тогл")
    @Nonnull
    public UserProfilePage switchArchiveToggle() {
        switchToggle.shouldBe(clickable).click();
        return this;
    }

    @Step("Проверяем отсутствие категории '{0}'")
    @Nonnull
    public UserProfilePage checkCategoryNotInList(String categoryName) {
        $x(String.format("//span[text()='%s']", categoryName)).shouldNotBe(visible);
        return this;
    }

    @Step("Проверка, что '{0}' есть в списке")
    @Nonnull
    public UserProfilePage checkCategoryInList(String categoryName) {
        $x(String.format("//span[text()='%s']", categoryName)).shouldBe(visible);
        return this;
    }

    @Step("Изменяем имя на '{0}'")
    @Nonnull
    public UserProfilePage setName(String name) {
        nameInput.clear();
        nameInput.setValue(name);
        return this;
    }

    @Step("Проверка, что имя поменялось на '{0}'")
    @Nonnull
    public UserProfilePage checkName(String name) {
        nameInput.shouldHave(value(name));
        return this;
    }

    @Nonnull
    public UserProfilePage checkPageIsLoaded() {
        uploadPickButton.shouldBe(visible);
        return this;
    }

    @Step("Проверка архивной категории: '{0}'")
    @Nonnull
    public UserProfilePage checkArchivedCategoryExists(String category) {
        switchToggle.shouldBe(clickable).click();
        bubblesArchived.find(text(category)).shouldBe(visible);
        return this;
    }
}
