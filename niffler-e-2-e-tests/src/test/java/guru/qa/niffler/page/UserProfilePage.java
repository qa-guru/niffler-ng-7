package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

@ParametersAreNonnullByDefault
public class UserProfilePage {

    public static String url = Config.getInstance().frontUrl() + "profile";

    private final SelenideElement
            dialogModal = $x("//div[@role='dialog']"),
            confirmButton = $x("//button[contains(text(),'chive')]"),
            switchToggle = $(".MuiSwitch-switchBase"),
            nameInput = $("#name"),
            uploadPickButton = $x("//label[@for='image__input']");


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
}
