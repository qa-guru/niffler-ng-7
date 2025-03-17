package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.PeoplesPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

@WebTest
public class FriendsWebTest {

    @User(
            friends = 1
    )
    @ApiLogin
    @Test
    void friendShouldBePresentInFriendsTable(UserJson user) {
        final String friendUsername = user.testData().friendsUsernames()[0];
        open(FriendsPage.URL, FriendsPage.class)
                .checkThatPageLoaded()
                .checkUserInUserFriendsList(friendUsername);
    }

    @User
    @ApiLogin
    @Test
    void friendShouldBeEmptyForNewUser() {
        open(FriendsPage.URL, FriendsPage.class)
                .checkThatPageLoaded()
                .checkFriendsIsEmpty();
    }

    @User(
            incomeInvitations = 2
    )
    @ApiLogin
    @Test
    void incomeInvitationShouldBePresentInFriendsTable(UserJson user) {
        final String friendUsername = user.testData().incomeInvitationsUsernames()[0];
        open(FriendsPage.URL, FriendsPage.class)
                .checkThatPageLoaded()
                .checkIncomeRequest(friendUsername);
    }

    @User(
            outcomeInvitations = 3
    )
    @ApiLogin
    @Test
    void outcomeInvitationShouldBePresentInAllPeopleTable(UserJson user) {
        final String friendUsername = user.testData().outcomeInvitationsUsernames()[0];
        open(PeoplesPage.URL, PeoplesPage.class)
                .checkThatPageLoaded()
                .checkOutcomeRequest(friendUsername);
    }

    @User(incomeInvitations = 1)
    @ApiLogin
    @Test
    void acceptInvitationTest(UserJson user) {
        final String userToAccept = user.testData().incomeInvitationsUsernames()[0];
        FriendsPage friendsPage = open(FriendsPage.URL, FriendsPage.class)
                .checkExistingInvitationsCount(1)
                .acceptFriendInvitationFromUser(userToAccept)
                .checkExistingInvitationsCount(0);

        Selenide.refresh();

        friendsPage.checkExistingFriendsCount(1)
                .checkExistingFriends(userToAccept);
    }

    @User(incomeInvitations = 1)
    @ApiLogin
    @Test
    void declineInvitationTest(UserJson user) {
        final String userToAccept = user.testData().incomeInvitationsUsernames()[0];
        FriendsPage friendsPage = open(FriendsPage.URL, FriendsPage.class)
                .checkExistingInvitationsCount(1)
                .declineFriendInvitationFromUser(userToAccept)
                .checkExistingInvitationsCount(0);

        Selenide.refresh();

        friendsPage.checkFriendsIsEmpty();
    }
}