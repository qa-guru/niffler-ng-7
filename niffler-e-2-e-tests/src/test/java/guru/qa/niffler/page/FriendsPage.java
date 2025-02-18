package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class FriendsPage extends BasePage<FriendsPage>{

    private final SelenideElement
            noFriendsTextElement = $("#simple-tabpanel-friends .MuiTypography-root"),
            searchInput = $("input[aria-label='search']"),
            requestsTable = $("#requests"),
            friendsTable = $("#friends"),
            popup = $("div[role='dialog']");

    @Step("Проверяем, что у пользователя нет друзей")
    public FriendsPage checkFriendsIsEmpty() {
        noFriendsTextElement.shouldHave(text("There are no users yet"));
        return this;
    }

    @Step("Проверяем, что '{0}' присутствует в списке друзей")
    @Nonnull
    public FriendsPage checkUserInUserFriendsList(String username) {
        searchInput.setValue(username).pressEnter();
        $("#friends").$$("tr").find(text(username)).should(visible);
        return this;
    }

    @Step("Проверяем, что '{0}' присутствует в заявках")
    @Nonnull
    public FriendsPage checkIncomeRequest(String username) {
        searchInput.setValue(username).pressEnter();
        $("#requests").$$("tr").find(text(username)).$$("button").shouldHave(texts("Accept", "Decline"));
        return this;
    }

    @Step("Проверяем, что во входящих заявках '{0}' человек")
    @Nonnull
    public FriendsPage checkExistingInvitationsCount(int expectedCount) {
        requestsTable.$$("tr").shouldHave(size(expectedCount));
        return this;
    }

    @Step("Принимаем заявку в друзья от '{0}'")
    @Nonnull
    public FriendsPage acceptFriendInvitationFromUser(String username) {
        SelenideElement friendRow = requestsTable.$$("tr").find(text(username));
        friendRow.$(byText("Accept")).click();
        return this;
    }

    @Step("Отклоняем заявку от пользователя: {username}")
    @Nonnull
    public FriendsPage declineFriendInvitationFromUser(String username) {
        SelenideElement friendRow = requestsTable.$$("tr").find(text(username));
        friendRow.$(byText("Decline")).click();
        popup.$(byText("Decline")).click(usingJavaScript());
        return this;
    }


    @Step("Проверяем, что в друзьях '{0}' человек")
    @Nonnull
    public FriendsPage checkExistingFriendsCount(int expectedCount) {
        friendsTable.$$("tr").shouldHave(size(expectedCount));
        return this;
    }

    @Step("Проверяем, что в списке друзей присутствует пользователь: '{0}'")
    @Nonnull
    public FriendsPage checkExistingFriends(String... expectedUsernames) {
        friendsTable.$$("tr").shouldHave(textsInAnyOrder(expectedUsernames));
        return this;
    }
}