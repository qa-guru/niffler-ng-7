package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage {
    private final SelenideElement
            friendsTable = $("#friends"),
            requestTable = $("#requests"),
            searchField = $("input[placeholder='Search']");

    public void checkThatFriendsExist(String... friendUserName) {
        friendsTable.$$("tr").shouldHave(textsInAnyOrder(friendUserName));
    }

    public void checkThatFriendsDoNotExist() {
        friendsTable.shouldNotBe(exist);
    }

    public void checkIncomeFriendRequest(String incomeUserName) {
        searchField.setValue(incomeUserName).pressEnter();
        requestTable.$$("tr").find(text(incomeUserName)).shouldBe(visible);
    }
}

