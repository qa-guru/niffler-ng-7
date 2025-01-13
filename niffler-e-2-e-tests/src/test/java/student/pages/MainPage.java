package student.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private final ElementsCollection
            tableRows = $("#spendings tbody").$$("tr"),
            menuTabs = $("ul[role='menu']").$$("li");
    private final SelenideElement
            historyOfSpendingsTitle = $(byText("History of Spendings")),
            profileButton = $("svg[data-testid='PersonIcon']");


    public MainPage verifyTitleMainPage() {
        historyOfSpendingsTitle.shouldBe(visible);
        return this;
    }

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$("td", 5).click();
        return new EditSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).shouldBe(visible);
    }

    public MainPage clickProfileIcon() {
        profileButton.shouldBe(visible).click();
        return this;
    }

    public ProfilePage clickProfileCategory() {
        menuTabs.find(text("Profile")).click();
        return new ProfilePage();
    }

    public FriendsPage clickFriendCategory() {
        menuTabs.find(text("Friends")).click();
        return new FriendsPage();
    }
}