package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.UserProfilePage;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static guru.qa.niffler.utils.RandomDataUtils.randomName;
import static guru.qa.niffler.utils.ScreenDiffResult.checkActualImageEqualsExpected;

@WebTest
public class ProfileTest {
    @User(
            categories = @Category(
                    archived = true
            )
    )
    @ApiLogin
    @Test
    void archivedCategoryShouldPresentInCategoriesList(UserJson user) {
        final String categoryName = user.testData().categoryDescriptions()[0];
        Selenide.open(UserProfilePage.URL, UserProfilePage.class)
                .checkThatPageLoaded()
                .checkArchivedCategoryExists(categoryName);
    }


    @User(
            categories = @Category(
                    archived = false
            )
    )
    @ApiLogin
    @Test
    void unArchivedCategoryShouldBePresentedInList(UserJson user) {
        final String categoryName = user.testData().categoryDescriptions()[0];
        Selenide.open(UserProfilePage.URL, UserProfilePage.class)
                .checkThatPageLoaded()
                .switchArchiveToggle()
                .checkCategoryInList(categoryName);
    }

    @User
    @ApiLogin
    @Test
    void userInfoShouldBeSavedAfterEditing() {
        final String name = randomName();
        Selenide.open(UserProfilePage.URL, UserProfilePage.class)
                .checkPageIsLoaded()
                .setName(name)
                .checkName(name);
    }

    @User
    @ApiLogin
    @ScreenShotTest(expected = "img/avatar.png")
    void checkProfileAvatar(BufferedImage expected) throws IOException {
        Selenide.open(UserProfilePage.URL, UserProfilePage.class)
                .checkPageIsLoaded();
        checkActualImageEqualsExpected(expected, ".MuiGrid-root [data-testId='PersonIcon']");
    }
}