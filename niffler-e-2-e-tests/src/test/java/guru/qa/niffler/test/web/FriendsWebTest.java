package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
public class FriendsWebTest {

    private static final Config CFG = Config.getInstance();

    @User(
            friends = 1
    )

    @Test
    void friendShouldBePresentInFriendsTable(UserJson user) {
        final String friendUsername = user.testData().friendsUsernames()[0];
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .checkIsLoaded()
                .openFriendsPage()
                .checkUserInUserFriendsList(friendUsername);
    }

    @User
    @Test
    void friendShouldBeEmptyForNewUser(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .checkIsLoaded()
                .openFriendsPage()
                .checkFriendsIsEmpty();
    }

    @User(
            incomeInvitations = 2
    )
    @Test
    void incomeInvitationShouldBePresentInFriendsTable(UserJson user) {
        final String friendUsername = user.testData().incomeInvitationsUsernames()[0];
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .checkIsLoaded()
                .openFriendsPage()
                .checkIncomeRequest(friendUsername);
    }

    @User(
            outcomeInvitations = 3
    )
    @Test
    void outcomeInvitationShouldBePresentInAllPeopleTable(UserJson user) {
        final String friendUsername = user.testData().outcomeInvitationsUsernames()[0];
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .checkIsLoaded()
                .openAllPeoplesPage()
                .checkOutcomeRequest(friendUsername);
    }
}