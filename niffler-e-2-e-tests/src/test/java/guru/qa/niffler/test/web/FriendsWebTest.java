package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.jupiter.extension.UserQueueExtension.*;
import guru.qa.niffler.jupiter.extension.UserQueueExtension.UserType.*;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith(BrowserExtension.class)
public class FriendsWebTest {
    private static final Config CFG = Config.getInstance();

    @Test
    @ExtendWith(UserQueueExtension.class)
    void friendShouldBePresentInFriendsTable(@UserType(Type.WITH_FRIEND) StaticUser user) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .clickByUserAvatar()
                .clickByFriends()
                .checkFriendInTable(user.friend());
    }

    @Test
    @ExtendWith(UserQueueExtension.class)
    void friendTableShouldBeEmptyForNewUser(@UserType(Type.EMPTY) StaticUser user) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .clickByUserAvatar()
                .clickByFriends()
                .checkSelectedTabByName("Friends")
                .checkEmptyTable();
    }

    @Test
    @ExtendWith(UserQueueExtension.class)
    void incomeInvitationBePresentInFriendsTable(@UserType(Type.WITH_INCOME_REQUEST) StaticUser user) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .clickByUserAvatar()
                .clickByFriends()
                .checkSelectedTabByName("Friends")
                .checkIncomeInTable(user.income());
    }

    @Test
    @ExtendWith(UserQueueExtension.class)
    void outcomeInvitationBePresentInAllPeoplesTable(@UserType(Type.WITH_OUTCOME_REQUEST) StaticUser user) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .clickByUserAvatar()
                .clickByAllPeople()
                .checkOutcomeInTable(user.outcome());
    }

}
