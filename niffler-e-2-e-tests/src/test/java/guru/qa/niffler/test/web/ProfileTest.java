package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.model.User.REGESTED_PASSWORD;
import static guru.qa.niffler.model.User.REGESTED_USERNAME;

public class ProfileTest {
    private static final Config CFG = Config.getInstance();

    @Category(
            username = REGESTED_USERNAME,
            archived = false
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(REGESTED_USERNAME, REGESTED_PASSWORD)
                .goToProfile()
                .checkCategoryIsDisplayed(category.name())
                .archiveCategory(category.name())
                .checkCategoryIsNotDisplayed(category.name())
                .clickShowArchivedSwitcher()
                .checkCategoryIsDisplayed(category.name());
    }

    @Category(
            username = REGESTED_USERNAME,
            archived = true
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(REGESTED_USERNAME, REGESTED_PASSWORD)
                .goToProfile()
                .checkCategoryIsNotDisplayed(category.name())
                .clickShowArchivedSwitcher()
                .checkCategoryIsDisplayed(category.name())
                .unarchiveCategory(category.name())
                .clickShowArchivedSwitcher()
                .checkCategoryIsDisplayed(category.name());
    }
}
