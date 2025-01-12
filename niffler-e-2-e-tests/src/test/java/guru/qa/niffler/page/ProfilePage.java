package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage {
    private final SelenideElement showArchivedSwitcher = $("input.PrivateSwitchBase-input.MuiSwitch-input[type=\"checkbox\"]");

    public ProfilePage clickShowArchivedSwitcher(){
        showArchivedSwitcher.scrollIntoView(false).click();
        return this;
    }

    public ProfilePage checkCategoryIsDisplayed(String category){
        $x("//span[text()='" + category + "']").shouldBe(visible);
        return this;
    }

    public ProfilePage checkCategoryIsNotDisplayed(String category){
        $x("//span[text()='" + category + "']").shouldNotBe(visible);
        return this;
    }

    public ProfilePage archiveCategory(String category){
        $x("//span[text()='" + category + "']/../..//button[@aria-label=\"Archive category\"]").click();
        $x("//button[text()='Archive']").click();
        return this;
    }

    public ProfilePage unarchiveCategory(String category){
        $x("//span[text()='" + category + "']/../..//button[@aria-label=\"Unarchive category\"]").click();
        $x("//button[text()='Unarchive']").click();
        return this;
    }
}
