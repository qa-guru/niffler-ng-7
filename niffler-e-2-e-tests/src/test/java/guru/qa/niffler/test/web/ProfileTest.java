package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class ProfileTest {
    private static final Config CFG = Config.getInstance();

    @Category(
            username = "user111",
            archived = true
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .loginSuccess(CFG.username(), CFG.password())
                .header.menuClick()
                .profileClick()
                .checkCategoryShouldBeArchived(category.name());
    }

    @Category(
            username = "user111",
            archived = false
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .loginSuccess(CFG.username(), CFG.password())
                .header.menuClick()
                .profileClick()
                .checkCategoryShouldBeActive(category.name());
    }

}
