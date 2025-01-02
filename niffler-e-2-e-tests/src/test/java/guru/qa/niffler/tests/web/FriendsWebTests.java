package guru.qa.niffler.tests.web;

import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUsers;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;

@DisplayName("Веб тесты на раздел 'Друзья'")
@WebTest
public class FriendsWebTests {

    @DisplayName("Друг должен присутствовать в таблице друзей")
    @Test
    void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUsers user) {
        new LoginPage()
                .open()
                .login(user.username(), user.password())
                .navigateMenuComponent
                .clickAccountMenuButton()
                .clickFriendsButton()
                .checkThatFriendsExist(user.friend());

    }
    @DisplayName("Таблица друзей должна быть пустой для нового пользователя")
    @Test
    void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUsers user) {
        new LoginPage()
                .open()
                .login(user.username(), user.password())
                .navigateMenuComponent
                .clickAccountMenuButton()
                .clickFriendsButton()
                .checkThatFriendsDoNotExist();
    }
    @DisplayName("Проверка отображения входящего запроса дружбы в таблице друзей")
    @Test
    void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUsers user) {
        new LoginPage()
                .open()
                .login(user.username(), user.password())
                .navigateMenuComponent
                .clickAccountMenuButton()
                .clickFriendsButton()
                .checkIncomeFriendRequest(user.income());
    }
    @DisplayName("Проверка отображения исходящего запроса дружбы в таблице людей")
    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) StaticUsers user) {
        new LoginPage()
                .open()
                .login(user.username(), user.password())
                .navigateMenuComponent
                .clickAccountMenuButton()
                .clickAllPeopleButton()
                .checkOutcomeFriendRequest(user.outcome());
    }
}