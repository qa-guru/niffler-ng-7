package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.components.SearchField;

import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage {

    private final SearchField searchField = new SearchField();

    private final SelenideElement
            friendsTable = $("#friends"),
            requestTable = $("#requests");

    public void checkThatFriendsExist(String... friendUserName) {
        friendsTable.$$("tr").shouldHave(textsInAnyOrder(friendUserName));
    }

    public void checkThatFriendsDoNotExist() {
        friendsTable.shouldNotBe(exist);
    }

    public void checkIncomeFriendRequest(String incomeUserName) {
        searchField.search(incomeUserName);
        requestTable.$$("tr").find(text(incomeUserName)).shouldBe(visible);
    }
}

