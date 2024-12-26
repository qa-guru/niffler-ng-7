package guru.qa.niffler.tests.web;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Тесты для страницы профиля пользователя")
public class ProfileTests {

    @Category(
            username = "Artur",
            archived = true
    )
    @DisplayName("Архивная категория должна присутствовать и отображаться в списке категорий")
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        new LoginPage()
                .open()
                .login(category.username(), "12345")
                .navigateMenuComponent
                .clickAccountMenuButton()
                .clickProfileButton()
                .clickArchivedCheckbox()
                .checkCategoryInCategoryList(category.name());
    }

    @Category(
            username = "Artur",
            archived = false
    )
    @DisplayName("Активная категория должна присутствовать и отображаться в списке категорий")
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        new LoginPage()
                .open()
                .login(category.username(), "12345")
                .navigateMenuComponent
                .clickAccountMenuButton()
                .clickProfileButton()
                .checkCategoryInCategoryList(category.name());
    }
}
