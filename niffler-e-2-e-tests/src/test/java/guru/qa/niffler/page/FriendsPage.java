package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage {

    private final SelenideElement
            noFriendsTextElement = $("#simple-tabpanel-friends .MuiTypography-root"),
            searchInput = $("input[aria-label='search']");

    public FriendsPage checkFriendsIsEmpty() {
        noFriendsTextElement.shouldHave(text("There are no users yet"));
        return this;
    }

    public FriendsPage checkUserInUserFriendsList(String username) {
        searchInput.setValue(username).pressEnter();
        $("#friends").$$("tr").find(text(username)).should(visible);
        return this;
    }

    public FriendsPage checkIncomeRequest(String username) {
        searchInput.setValue(username).pressEnter();
        $("#requests").$$("tr").find(text(username)).$$("button").shouldHave(texts("Accept", "Decline"));
        return this;
    }
}