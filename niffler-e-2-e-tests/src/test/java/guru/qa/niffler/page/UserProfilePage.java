package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class UserProfilePage {

    public static String url = Config.getInstance().frontUrl()+ "profile";

    private final SelenideElement
            dialogModal = $x("//div[@role='dialog']"),
            confirmButton = $x("//button[contains(text(),'chive')]"),
            switchToggle = $(".MuiSwitch-switchBase");


    public UserProfilePage changeArchiveCategoryStateByName(String category, String state) {
        SelenideElement archiveButton = $x(String.format("//span[text()='%s']/../..//button[@aria-label='%s category']", category, state));
        archiveButton.shouldBe(clickable).click();
        return this;
    }


    public UserProfilePage confirmArchiveCategory() {
        dialogModal.shouldBe(visible);
        confirmButton.click();
        $(".MuiAlert-message").shouldBe(visible);
        return this;
    }

    public UserProfilePage switchArchiveToggle() {
        switchToggle.shouldBe(clickable).click();
        return this;
    }

    public UserProfilePage checkCategoryNotInList(String categoryName) {
        $x(String.format("//span[text()='%s']", categoryName)).shouldNotBe(visible);
        return this;
    }

    public UserProfilePage checkCategoryInList(String categoryName) {
        $x(String.format("//span[text()='%s']", categoryName)).shouldBe(visible);
        return this;
    }
}
