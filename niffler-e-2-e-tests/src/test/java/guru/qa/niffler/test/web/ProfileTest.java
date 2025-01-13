package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.config.Constants;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class ProfileTest {
    private static final Config CFG = Config.getInstance();

    @Test
    @Category(
            username = Constants.userName,
            archived = true)
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(Constants.userName, Constants.password)
                .clickByUserAvatar()
                .clickByProfile()
                .activatedShowArchiveCategory()
                .checkNameSpendInCategoryList(category.name());
    }

    @Test
    @Category(
            username = Constants.userName,
            archived = false)
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(Constants.userName, Constants.password)
                .clickByUserAvatar()
                .clickByProfile()
                .checkNameSpendInCategoryList(category.name());
    }

}
