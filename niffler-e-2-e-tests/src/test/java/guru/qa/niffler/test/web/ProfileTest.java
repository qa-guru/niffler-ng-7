package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.config.Constants;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
public class ProfileTest {
    private static final Config CFG = Config.getInstance();

    @Test
    @User(username = Constants.MAIN_USERNAME,
            categories = @Category(archived = true))
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(Constants.MAIN_USERNAME, Constants.MAIN_PASSWORD)
                .clickByUserAvatar()
                .clickByProfile()
                .activatedShowArchiveCategory()
                .checkNameSpendInCategoryList(category.name());
    }

    @Test
    @User(username = Constants.MAIN_USERNAME,
            categories = @Category(archived = false))
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(Constants.MAIN_USERNAME, Constants.MAIN_PASSWORD)
                .clickByUserAvatar()
                .clickByProfile()
                .checkNameSpendInCategoryList(category.name());
    }

}
