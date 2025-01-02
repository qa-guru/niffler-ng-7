package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage {
    private final SelenideElement
            friendsTable = $("#friends"),
            requestTable = $("#requests");

    public void checkThatFriendsExist(String friendUserName) {
        friendsTable.$$("tr").find(text(friendUserName)).shouldBe(visible);
    }

    public void checkThatFriendsDoNotExist() {
        friendsTable.shouldNotBe(exist);
    }

    public void checkIncomeFriendRequest(String incomeUserName) {
        requestTable.$$("tr").find(text(incomeUserName)).shouldBe(visible);
    }
}

