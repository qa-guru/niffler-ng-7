package quru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import quru.qa.niffler.config.Config;
import quru.qa.niffler.jupiter.BrowserExtension;
import quru.qa.niffler.jupiter.category.Category;
import quru.qa.niffler.model.CategoryJson;
import quru.qa.niffler.page.LoginPage;

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
                .shouldSeeCategoryInCategoryList(category.name(), category.archived());
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
                .shouldSeeCategoryInCategoryList(category.name(), category.archived());
    }
}
