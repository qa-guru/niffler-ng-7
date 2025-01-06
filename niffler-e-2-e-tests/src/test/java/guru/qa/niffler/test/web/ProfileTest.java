package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
public class ProfileTest {

    private static final Config CFG = Config.getInstance();
    private static final String USERNAME = "maria";
    private static final String PW = "12345";


    @User(
            username = USERNAME,
            categories = {
                    @Category(
                            archived = false
                    )
            }
    )
    @Test
    public void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) throws InterruptedException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(USERNAME, PW)
                .openProfilePage()
                .profilePageIsOpened()
                .checkThatActiveCategoryIsPresented(category.name())
                .archiveCategory(category.name())
                .showArchivedCategories()
                .checkThatArchivedCategoryIsPresented(category.name());
    }

    @User(
            username = USERNAME,
            categories = {
                    @Category(
                            archived = true
                    )
            }
    )
    @Test
    public void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(USERNAME, PW)
                .openProfilePage()
                .profilePageIsOpened()
                .showArchivedCategories()
                .checkThatArchivedCategoryIsPresented(category.name())
                .unArchiveCategory(category.name())
                .checkThatActiveCategoryIsPresented(category.name());
    }
}
