package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import jupiter.BrowserExtension;
import jupiter.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class ProfileTest {

    private static final Config CFG = Config.getInstance();
    private static final String USERNAME = "maria";
    private static final String PW = "12345";

    @Category(
        username = USERNAME,
        archived = false
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

    @Category(
            username = USERNAME,
            archived = true
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
