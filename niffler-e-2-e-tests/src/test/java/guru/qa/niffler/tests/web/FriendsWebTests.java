package guru.qa.niffler.tests.web;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("Веб тесты на раздел 'Друзья'")
@WebTest
public class FriendsWebTests {

    @User(
            friends = 1
    )
    @DisplayName("Друг должен присутствовать в таблице друзей")
    @Test
    void friendShouldBePresentInFriendsTable(UserJson user) {
        final String friendUsername = user.testData().friendsUsernames()[0];
        new LoginPage()
                .open()
                .login(user.username(), user.testData().password())
                .navigateMenuComponent
                .clickAccountMenuButton()
                .clickFriendsButton()
                .checkThatFriendsExist(friendUsername);

    }

    @User
    @DisplayName("Таблица друзей должна быть пустой для нового пользователя")
    @Test
    void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        new LoginPage()
                .open()
                .login(user.username(), user.testData().password())
                .navigateMenuComponent
                .clickAccountMenuButton()
                .clickFriendsButton()
                .checkThatFriendsDoNotExist();
    }

    @User(incomeInvitations = 1)
    @DisplayName("Проверка отображения входящего запроса дружбы в таблице друзей")
    @Test
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        new LoginPage()
                .open()
                .login(user.username(), user.testData().password())
                .navigateMenuComponent
                .clickAccountMenuButton()
                .clickFriendsButton()
                .checkIncomeFriendRequest(user.testData().incomeInvitationsUsernames()[0]);
    }

    @User(outcomeInvitations = 1)
    @DisplayName("Проверка отображения исходящего запроса дружбы в таблице людей")
    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        new LoginPage()
                .open()
                .login(user.username(), user.testData().password())
                .navigateMenuComponent
                .clickAccountMenuButton()
                .clickAllPeopleButton()
                .checkOutcomeFriendRequest(user.testData().outcomeInvitationsUsernames()[0]);
    }
}