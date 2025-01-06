package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;

@WebTest
public class FriendsWebTest {

    private static final Config CFG = Config.getInstance();
    private static final String EMPTY_FRIENDS_LIST_TEXT = "There are no users yet";

    @Test
    void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.password())
                .openFriendsPage()
                .checkIsLoaded()
                .checkUserHasFriendWithName(user.friend());
    }

    @Test
    void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.password())
                .openFriendsPage()
                .checkIsLoaded()
                .checkFriendsListHasText(EMPTY_FRIENDS_LIST_TEXT);
    }

    @Test
    void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) {

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.password())
                .openFriendsPage()
                .checkIsLoaded()
                .checkIncomeRequestHasName(user.income());
    }

    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.password())
                .openFriendsPage()
                .checkIsLoaded()
                .openAllPeopleTab()
                .checkOutcomeRequestHasName(user.outcome());
    }

}
