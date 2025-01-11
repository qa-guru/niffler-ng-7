package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage {
    private final SelenideElement emptyTabPanelText = $("#simple-tabpanel-friends").$("p");
    private final ElementsCollection friendsTableRows = $$("#friends tr");
    private final ElementsCollection requestsTableRows = $$("#requests tr");

    public void shouldSeeEmptyTabPanelFriends() {
        emptyTabPanelText.should(visible);
        String expected = "There are no users yet";
        emptyTabPanelText.shouldHave(text(expected));
    }

    public void shouldSeeFriendInFriendsTable(String friendName) {
        friendsTableRows.find(text(friendName)).should(visible);
    }

    public void shouldSeeFriendNameRequestInRequestsTable(String friendNameRequest) {
        requestsTableRows.find(text(friendNameRequest)).should(visible);
    }

}
