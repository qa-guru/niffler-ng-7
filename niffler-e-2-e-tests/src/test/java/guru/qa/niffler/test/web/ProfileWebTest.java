package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
public class ProfileWebTest {

    private static final Config CFG = Config.getInstance();

    @User(
            username = "filkot",
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void archivedCategoryShouldNotPresentInCategoryList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("filkot", "12345")
                .addNewSpending()
                .shouldNotSeeArchivedCategoryInCategoryList(category.name());
    }

    @User(
            username = "filkot",
            categories = @Category(
                    archived = false
            )
    )
    @Test
    void activeCategoryShouldPresentInCategoryList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("filkot", "12345")
                .addNewSpending()
                .shouldSeeActiveCategoryInCategoryList(category.name());
    }
}