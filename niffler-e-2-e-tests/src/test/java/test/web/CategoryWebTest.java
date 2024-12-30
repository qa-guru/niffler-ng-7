package test.web;

import com.codeborne.selenide.Selenide;
import config.Config;
import jupiter.browser.BrowserExtension;
import jupiter.category.Category;
import model.CategoryJson;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.LoginPage;

import static util.DataGenerator.*;

@ExtendWith(BrowserExtension.class)
public class CategoryWebTest {

    private static final Config CFG = Config.getInstance();
    @Test
    @Category(
            username = userName,
            archived = true
    )
    void archivedCategoryShouldBePresentedInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setUserName(userName)
                .setPassword(userPassword)
                .clickSubmitButton()
                .clickProfileIcon()
                .clickProfileCategory()
                .clickArchivedCheckbox()
                .checkCategoryInCategoryList(category.name());
    }

    @Test
    @Category(
            username = userName,
            archived = false
    )
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setUserName(userName)
                .setPassword(userPassword)
                .clickSubmitButton()
                .clickProfileIcon()
                .clickProfileCategory()
                .checkCategoryInCategoryList(category.name());
    }
}
