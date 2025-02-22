package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.config.Constants;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({BrowserExtension.class, UserQueueExtension.class})
public class ProfileTest {
    private static final Config CFG = Config.getInstance();

    @Test
    @Category(
            username = Constants.MAIN_USERNAME,
            archived = true)
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(Constants.MAIN_USERNAME, Constants.MAIN_PASSWORD)
                .clickByUserAvatar()
                .clickByProfile()
                .activatedShowArchiveCategory()
                .checkNameSpendInCategoryList(category.name());
    }

    @Test
    @Category(
            username = Constants.MAIN_USERNAME,
            archived = false)
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(Constants.MAIN_USERNAME, Constants.MAIN_PASSWORD)
                .clickByUserAvatar()
                .clickByProfile()
                .checkNameSpendInCategoryList(category.name());
    }

}
