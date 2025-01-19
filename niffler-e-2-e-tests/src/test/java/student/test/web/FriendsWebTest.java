package student.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import student.config.Config;
import student.jupiter.annotaion.UserType;
import student.jupiter.annotaion.meta.WebTest;
import student.pages.LoginPage;

import static student.jupiter.annotaion.UserType.Type.*;
import static student.jupiter.extension.user.UsersQueueExtension.StaticUser;

@WebTest
public class FriendsWebTest {
    private static final Config CFG = Config.getInstance();

    @Test
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
    void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {
        Selenide
                .open(CFG.frontUrl(), LoginPage.class)
                .enterWithLoginAndPassword(user.userName(), user.password())
                .clickProfileIcon()
                .clickFriendCategory()
                .checkNoFriendsText();
    }

    @Test
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
