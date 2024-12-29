package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {
    private final SelenideElement
            historyOfSpendingsText = $("#spendings h2"),
            avatarIcon = $(".MuiAvatar-root"),
            uploadPickButton = $x("//label[@for='image__input']"),
            placeholder = $("input[placeholder]");
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");

    private static SelenideElement getMenuElement(String hrefValue){
        return $x(String.format("//a[@href='/%s']", hrefValue));
    }

    public MainPage checkIsLoaded() {
        historyOfSpendingsText.shouldHave(text("History of Spendings"));
        return this;
    }

    public UserProfilePage openProfilePage() {
        avatarIcon.click();
        getMenuElement("profile").click();
        uploadPickButton.shouldBe(visible);
        return new UserProfilePage();
    }

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td") .get(5).click();
        return new EditSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

    public FriendsPage openFriendsPage() {
        avatarIcon.click();
        getMenuElement("people/friends").click();
        placeholder.shouldBe(visible);
        return new FriendsPage();
    }

    public PeoplesPage openAllPeoplesPage() {
        avatarIcon.click();
        getMenuElement("people/all").click();
        placeholder.shouldBe(visible);
        return new PeoplesPage();
    }
}