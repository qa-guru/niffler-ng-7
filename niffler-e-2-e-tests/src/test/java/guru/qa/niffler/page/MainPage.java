package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;


import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {
    //Toolbar
    private final SelenideElement profileBtn = $("button div.MuiAvatar-root");
    //Profile Menu
    private final SelenideElement profileMenuItem = $("a[href='/profile']");
    //Spending
    private final SelenideElement historyOfSpendingHeader = $(By.xpath("//h2[text() = 'History of Spendings']"));
    private final SelenideElement searchBar =$("input[placeholder='Search']");
    private final ElementsCollection tableRows = $$("#spendings tbody tr");
    //Statistics
    private final SelenideElement statisticsHeader = $(By.xpath("//h2[text() = 'Statistics']"));
    private final SelenideElement statisticsImg = $("canvas[role='img']");
    private final SelenideElement legendBox = $("div#legend-container");

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription))
                .$$("td")
                .get(5)
                .click();
        return new EditSpendingPage();
    }

    public void checkThatTableContains(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

    public void checkIsLoaded() {
        statisticsHeader.should(visible);
        statisticsImg.should(visible);
        legendBox.should(visible);
        historyOfSpendingHeader.should(visible);
        searchBar.should(visible);
    }

    public ProfilePage openProfilePage() {
        profileBtn.click();
        profileMenuItem.click();
        return new ProfilePage();
    }
}
