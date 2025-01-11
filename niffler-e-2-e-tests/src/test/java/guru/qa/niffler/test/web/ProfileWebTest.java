package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;

@ExtendWith(BrowserExtension.class)
public class ProfileWebTest {

    private static final Config CFG = Config.getInstance();

    @Category(
            username = "filkot",
            archived = true
    )
    @Test
    void archivedCategoryShouldNotPresentInCategoryList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("filkot", "12345")
                .addNewSpending()
                .shouldNotSeeArchivedCategoryInCategoryList(category.name());
    }

    @Category(
            username = "filkot",
            archived = false
    )
    @Test
    void activeCategoryShouldPresentInCategoryList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("filkot", "12345")
                .addNewSpending()
                .shouldSeeActiveCategoryInCategoryList(category.name());
    }
}
