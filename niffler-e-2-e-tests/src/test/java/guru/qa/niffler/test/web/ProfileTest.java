package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;


public class ProfileTest {
    private static final Config CFG = Config.getInstance();

    @Category(
            username = "taty",
            archived = false
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

    @Category(
            username = "taty",
            archived = true
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
}