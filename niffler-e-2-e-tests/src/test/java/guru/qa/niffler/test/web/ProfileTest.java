package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static guru.qa.niffler.utils.RandomDataUtils.randomName;
import static guru.qa.niffler.utils.ScreenDiffResult.checkActualImageEqualsExpected;


public class ProfileTest {
    private static final Config CFG = Config.getInstance();

    @User(
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(UserJson user) {
        final String categoryName = user.testData().categoryDescriptions()[0];

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .toProfilePage()
                .checkThatPageLoaded()
                .checkArchivedCategoryExists(categoryName);
    }


    @User(
            categories = @Category(
                    archived = false
            )
    )
    @Test
    void unArchivedCategoryShouldBePresentedInList(UserJson user) {
        final String categoryName = user.testData().categoryDescriptions()[0];
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), "123")
                .getHeader()
                .toProfilePage()
                .checkThatPageLoaded()
                .switchArchiveToggle()
                .checkCategoryInList(categoryName);
    }

    @User
    @Test
    void userInfoShouldBeSavedAfterEditing(UserJson user) {
        final String name = randomName();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .toProfilePage()
                .checkPageIsLoaded()
                .setName(name)
                .checkName(name);
    }

    @User
    @ScreenShotTest("img/avatar.png")
    void checkProfileAvatar(UserJson user, BufferedImage expected) throws IOException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .toProfilePage()
                .checkPageIsLoaded();
        checkActualImageEqualsExpected(expected, ".MuiGrid-root [data-testId='PersonIcon']");
    }
}