package guru.qa.niffler.tests.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.PeoplePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("Веб тесты на раздел 'Друзья'")
@WebTest
public class FriendsWebTests {

    @DisplayName("Друг должен присутствовать в таблице друзей")
    @User(
            friends = 1
    )
    @ApiLogin
    @Test
    void friendShouldBePresentInFriendsTable(UserJson user) {
        final String friendUsername = user.testData().friendsUsernames()[0];

        Selenide.open(FriendsPage.FRIENDS_PAGE_URL, FriendsPage.class)
                .checkThatFriendsExist(friendUsername);

    }

    @DisplayName("Таблица друзей должна быть пустой для нового пользователя")
    @User
    @ApiLogin
    @Test
    void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        Selenide.open(FriendsPage.FRIENDS_PAGE_URL, FriendsPage.class)
                .checkThatFriendsDoNotExist();
    }

    @DisplayName("Проверка отображения входящего запроса дружбы в таблице друзей")
    @User(incomeInvitations = 1)
    @ApiLogin
    @Test
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        Selenide.open(FriendsPage.FRIENDS_PAGE_URL, FriendsPage.class)
                .checkIncomeFriendRequest(user.testData().incomeInvitationsUsernames()[0]);
    }

    @DisplayName("Проверка отображения исходящего запроса дружбы в таблице людей")
    @User(outcomeInvitations = 1)
    @ApiLogin
    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        Selenide.open(PeoplePage.PEOPLE_PAGE_URL, PeoplePage.class)
                .checkOutcomeFriendRequest(user.testData().outcomeInvitationsUsernames()[0]);
    }

    @DisplayName("Проверка возможности принять входящее приглашение дружбы")
    @User(incomeInvitations = 1)
    @ApiLogin
    @Test
    void acceptInvitation(UserJson user) {
        final String userIncome = user.testData().incomeInvitationsUsernames()[0];

        Selenide.open(FriendsPage.FRIENDS_PAGE_URL, FriendsPage.class)
                .acceptFriendInvitation(userIncome)
                .checkThatFriendAccepted(userIncome);
    }

    @DisplayName("Проверка возможности отклонить входящее приглашение дружбы")
    @User(incomeInvitations = 1)
    @ApiLogin
    @Test
    void declineInvitation(UserJson user) {
        final String userIncome = user.testData().incomeInvitationsUsernames()[0];

        Selenide.open(FriendsPage.FRIENDS_PAGE_URL, FriendsPage.class)
                .declineFriendInvitation(userIncome)
                .checkThatFriendsTableEmpty();
    }

}