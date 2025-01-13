package student.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import student.config.Config;
import student.jupiter.annotaion.UserType;
import student.jupiter.extension.browser.BrowserExtension;
import student.jupiter.extension.user.UsersQueueExtension;
import student.pages.LoginPage;

import static student.jupiter.annotaion.UserType.Type.*;
import static student.jupiter.extension.user.UsersQueueExtension.StaticUser;

@ExtendWith(BrowserExtension.class)
public class FriendsWebTest {
    private static final Config CFG = Config.getInstance();

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUser user) {
        Selenide
                .open(CFG.frontUrl(), LoginPage.class)
                .enterWithLoginAndPassword(user.userName(), user.password())
                .clickProfileIcon()
                .clickFriendCategory()
                .choseFriends()
                .friendShouldBePresentInFriendsByUserName(user.friend());
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {
        Selenide
                .open(CFG.frontUrl(), LoginPage.class)
                .enterWithLoginAndPassword(user.userName(), user.password())
                .clickProfileIcon()
                .clickFriendCategory()
                .checkNoFriendsText();
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) {
        Selenide
                .open(CFG.frontUrl(), LoginPage.class)
                .enterWithLoginAndPassword(user.userName(), user.password())
                .clickProfileIcon()
                .clickFriendCategory()
                .choseFriends()
                .incomeInvitationBePresentByUserName(user.income());
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void outcomeInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
        Selenide
                .open(CFG.frontUrl(), LoginPage.class)
                .enterWithLoginAndPassword(user.userName(), user.password())
                .clickProfileIcon()
                .clickFriendCategory()
                .choseAllPeople()
                .outcomeInvitationBePresentByUserName(user.outcome());
    }
}
