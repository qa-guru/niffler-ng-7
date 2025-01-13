package student.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage {

    private final SelenideElement
            peopleTab = $("div[role='tabpanel']");

    private final ElementsCollection
            sectionTab = $("div[aria-label='People tabs']").$$("a"),
            allUsersList = $("table[aria-labelledby='tableTitle']").$$("tr");

    public FriendsPage checkNoFriendsText() {
        peopleTab.shouldHave(text("There are no users yet"));
        return this;
    }

    public FriendsPage choseAllPeople() {
        sectionTab.find(text("All people")).click();
        return this;
    }

    public FriendsPage choseFriends() {
        sectionTab.find(text("Friends")).click();
        return this;
    }

    public FriendsPage outcomeInvitationBePresentByUserName(String userName) {
        allUsersList.find(text(userName)).shouldHave(text("Waiting..."));
        return this;
    }

    public FriendsPage incomeInvitationBePresentByUserName(String userName) {
        allUsersList.find(text(userName)).shouldHave(text("Accept"));
        return this;
    }

    public FriendsPage friendShouldBePresentInFriendsByUserName(String userName) {
        allUsersList.find(text(userName)).shouldHave(text("Unfriend"));
        return this;
    }
}
