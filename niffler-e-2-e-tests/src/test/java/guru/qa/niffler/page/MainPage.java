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
            profileLink = $x("//a[@href='/profile']"),
            uplpadPickButoon = $x("//label[@for='image__input']");
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");


    public MainPage checkSuccessLogin() {
        historyOfSpendingsText.shouldHave(text("History of Spendings"));
        return this;
    }

    public UserProfilePage openProfilePage() {
        avatarIcon.click();
        profileLink.click();
        uplpadPickButoon.shouldBe(visible);
        return new UserProfilePage();
    }

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

}