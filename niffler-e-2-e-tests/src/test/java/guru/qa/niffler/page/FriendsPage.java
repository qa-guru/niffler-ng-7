package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.components.Header;
import guru.qa.niffler.page.components.SearchField;
import io.qameta.allure.Step;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Getter
@ParametersAreNonnullByDefault
public class FriendsPage {

    private final SearchField searchField = new SearchField();
    private final Header header = new Header();

    private final SelenideElement
            friendsTable = $("#friends"),
            requestTable = $("#requests"),
            popup = $("div[role='dialog']"),
            simpleTableFriend = $("div[id='simple-tabpanel-friends']");

    @Step("Принятие входящего приглашения дружбы от <username>")
    @Nonnull
    public FriendsPage acceptFriendInvitation(String username) {
        requestTable.$$("tr").find(text(username))
                .$(byText("Accept")).click();
        return this;
    }

    @Step("Отклонение входящего приглашения дружбы от <username>")
    @Nonnull
    public FriendsPage declineFriendInvitation(String username) {
        requestTable.$$("tr").find(text(username))
                .$(byText("Decline")).click();
        popup.$(byText("Decline")).click(usingJavaScript());
        return this;
    }

    @Step("Проверка, что список друзей содержит <friendUserName>")
    public void checkThatFriendsExist(String... friendUserName) {
        friendsTable.$$("tr").shouldHave(textsInAnyOrder(friendUserName));
    }

    @Step("Проверка, что приглашение в друзья для <friendUserName> принято")
    public void checkThatFriendAccepted(String friendUserName) {
        requestTable.$$("tr").find(text(friendUserName))
                .$(byText("Unfriend")).shouldBe(exist);
    }

    @Step("Проверка, что список друзей пустой")
    public void checkThatFriendsDoNotExist() {
        friendsTable.shouldNotBe(exist);
    }

    @Step("Проверка входящего запроса для дружбы")
    public void checkIncomeFriendRequest(String incomeUserName) {
        searchField.search(incomeUserName);
        requestTable.$$("tr").find(text(incomeUserName)).shouldBe(visible);
    }

    @Step("Проверка входящего запроса для дружбы")
    public void checkThatFriendsTableEmpty() {
        simpleTableFriend.shouldHave(text("There are no users yet"));
    }
}

