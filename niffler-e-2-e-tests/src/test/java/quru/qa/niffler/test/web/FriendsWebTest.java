package quru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import quru.qa.niffler.config.Config;
import quru.qa.niffler.jupiter.extension.BrowserExtension;
import quru.qa.niffler.jupiter.extension.UsersQueueExtension;
import quru.qa.niffler.page.LoginPage;

import static quru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import static quru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import static quru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;

@ExtendWith(BrowserExtension.class)
public class FriendsWebTest {

    private static final Config CFG = Config.getInstance();


    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .openFriendsPage()
                .shouldSeeEmptyTabPanelFriends();
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .openFriendsPage()
                .shouldSeeFriendInFriendsTable(user.friend());

    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .openFriendsPage()
                .shouldSeeFriendNameRequestInRequestsTable(user.income());

    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void outcomeInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .openAllPeoplePage()
                .shouldSeeOutcomeInvitationInAllPeoplesTable(user.outcome());
    }
}
