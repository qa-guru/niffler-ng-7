package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class HeaderComponent {
    private final SelenideElement menuButton = $("header button[aria-label='Menu']");
    private final SelenideElement newSpendingHref = $("a[href='/spending']");
    private final SelenideElement menuProfile = $("a[href='/profile']");

    public HeaderComponent menuClick() {
        menuButton.should(visible);
        menuButton.click();
        return this;
    }

    public ProfilePage profileClick() {
        menuProfile.should(visible);
        menuProfile.click();
        return new ProfilePage();
    }

}
