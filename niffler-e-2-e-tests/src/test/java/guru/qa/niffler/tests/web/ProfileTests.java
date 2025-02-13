package guru.qa.niffler.tests.web;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("Тесты для страницы профиля пользователя")
@WebTest
public class ProfileTests {

    @User(
            username = "Artur",
            categories = @Category( archived = true)
    )
    @DisplayName("Архивная категория должна присутствовать и отображаться в списке категорий")
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson[] category) {
        new LoginPage()
                .open()
                .login(category[0].username(), "12345")
                .getHeader()
                .toProfilePage()
                .clickArchivedCheckbox()
                .checkCategoryInCategoryList(category[0].name());
    }

    @User(
            username = "Artur",
            categories = @Category()
    )
    @DisplayName("Активная категория должна присутствовать и отображаться в списке категорий")
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        new LoginPage()
                .open()
                .login(category.username(), "12345")
                .getHeader()
                .toProfilePage()
                .checkCategoryInCategoryList(category.name());
    }

    @User
    @Test
    void updateAllFieldsProfile(UserJson user) {
        new LoginPage()
                .open()
                .login(user.username(), user.testData().password())
                .getHeader()
                .toProfilePage()
                .uploadImage("image/duck.jpg")
                .setName(RandomDataUtils.randomName())
                .setNewCategory(RandomDataUtils.randomCategoryName())
                .saveChanges()
                .checkAlertMessage("Profile successfully updated");
    }
}
