package guru.qa.niffler.tests.web;

import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUsers;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;

@ExtendWith(BrowserExtension.class)
public class FriendsWebTests {

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUsers user) {
        new LoginPage()
                .open()
                .login(user.username(), user.password())
                .navigateMenuComponent
                .clickAccountMenuButton()
                .clickFriendsButton()
                .checkThatFriendsExist(user.friend());

    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUsers user) {
        new LoginPage()
                .open()
                .login(user.username(), user.password())
                .navigateMenuComponent
                .clickAccountMenuButton()
                .clickFriendsButton()
                .checkThatFriendsDoNotExist();
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUsers user) {
        new LoginPage()
                .open()
                .login(user.username(), user.password())
                .navigateMenuComponent
                .clickAccountMenuButton()
                .clickFriendsButton()
                .checkIncomeFriendRequest(user.income());
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
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