package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.api.CategoryApiClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class ProfileWebTest {
    private static final Config CFG = Config.getInstance();
    CategoryApiClient categoryApiClient = new CategoryApiClient();

    @Category(
        username = "succesname",
            archived = false
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson categoryJson){
        Selenide.open(CFG.frontUrl(), LoginPage.class).login("succesname","succespass");
        categoryApiClient.editCategory(categoryJson);
        Selenide.open(CFG.profileUrl(), ProfilePage.class).clickShowArchiveBtnField().archivedCategoryBtnShouldBeVisible();
    }

    @Category(
            username = "succesname",
            archived = false
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(){
        Selenide.open(CFG.frontUrl(), LoginPage.class).login("succesname","succespass");
        Selenide.open(CFG.profileUrl(), ProfilePage.class).activeCategoryShouldBeVisible();
    }
}
