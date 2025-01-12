package student.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import student.config.Config;
import student.jupiter.annotaion.Category;
import student.jupiter.extension.browser.BrowserExtension;
import student.model.CategoryJson;
import student.pages.LoginPage;
import student.util.DataGenerator;

@ExtendWith(BrowserExtension.class)
public class CategoryWebTest {

    private static final Config CFG = Config.getInstance();
    @Test
    @Category(
            username = DataGenerator.userName,
            archived = true
    )
    void archivedCategoryShouldBePresentedInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setUserName(DataGenerator.userName)
                .setPassword(DataGenerator.userPassword)
                .clickSubmitButton()
                .clickProfileIcon()
                .clickProfileCategory()
                .clickArchivedCheckbox()
                .checkCategoryInCategoryList(category.name());
    }

    @Test
    @Category(
            username = DataGenerator.userName,
            archived = false
    )
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setUserName(DataGenerator.userName)
                .setPassword(DataGenerator.userPassword)
                .clickSubmitButton()
                .clickProfileIcon()
                .clickProfileCategory()
                .checkCategoryInCategoryList(category.name());
    }
}
