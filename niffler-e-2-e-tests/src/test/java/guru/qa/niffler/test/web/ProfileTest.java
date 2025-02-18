package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.CategoryJson;

import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.RandomDataUtils.randomName;


public class ProfileTest {
    private static final Config CFG = Config.getInstance();

    @User(
            username = "taty",
            categories = @Category(
                    archived = false
            )
    )
    @Test
    void archivedCategoryShouldBePresentedInList(CategoryJson categoryJson) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(categoryJson.username(), "123")
                .openProfilePage()
                .changeArchiveCategoryStateByName(categoryJson.name(), "Archive")
                .confirmArchiveCategory()
                .checkCategoryNotInList(categoryJson.name())
                .switchArchiveToggle()
                .checkCategoryInList(categoryJson.name());
    }

    @User(
            username = "taty",
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void unArchivedCategoryShouldBePresentedInList(CategoryJson categoryJson) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(categoryJson.username(), "123")
                .openProfilePage()
                .switchArchiveToggle()
                .changeArchiveCategoryStateByName(categoryJson.name(), "Unarchive")
                .confirmArchiveCategory()
                .switchArchiveToggle()
                .checkCategoryInList(categoryJson.name());
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
}