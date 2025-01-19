package student.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import student.config.Config;
import student.jupiter.annotaion.Category;
import student.jupiter.annotaion.meta.User;
import student.jupiter.annotaion.meta.WebTest;
import student.model.CategoryJson;
import student.pages.LoginPage;
import student.util.DataGenerator;

@WebTest
public class CategoryWebTest {

    private static final Config CFG = Config.getInstance();
    @Test
    @User(
            username = DataGenerator.userName,
            categories = @Category(archived = true)
    )
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
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
    @User(
            username = DataGenerator.userName,
            categories = @Category(archived = false)
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
