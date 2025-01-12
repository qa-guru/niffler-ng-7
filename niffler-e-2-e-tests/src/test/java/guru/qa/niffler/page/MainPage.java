package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {
    private final SelenideElement statistics = $("#stat");
    private final SelenideElement historyOfSpendings = $("#spendings");
    private final ElementsCollection tableRows = $$("#spendings tbody tr");
    private final SelenideElement menuButton = $("button[aria-label=\"Menu\"]");
    private final SelenideElement profileButton = $("a.link.nav-link[href=\"/profile\"]");

    public EditSpendingPage findSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription))
                .$$("td")
                .get(5)
                .click();
        return new EditSpendingPage();
    }

    public void checkMainPageIsOpen() {
        statistics.shouldBe(visible);
        historyOfSpendings.shouldBe(visible);
    }

    public void checkThatTableContains(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

    public ProfilePage goToProfile() {
        menuButton.click();
        profileButton.click();
        return new ProfilePage();
    }
}
