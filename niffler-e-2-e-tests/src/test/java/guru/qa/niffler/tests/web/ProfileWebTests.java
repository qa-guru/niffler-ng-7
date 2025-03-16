package guru.qa.niffler.tests.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;


@DisplayName("Тесты для страницы профиля пользователя")
@WebTest
public class ProfileWebTests {

    @User(
            username = "Artur",
            categories = @Category(archived = true)
    )
    @ApiLogin
    @DisplayName("Архивная категория должна присутствовать и отображаться в списке категорий")
    @Test
    void archivedCategoryShouldPresentInCategoriesList(@NotNull UserJson user) {
        Selenide.open(ProfilePage.PROFILE_PAGE_URL, ProfilePage.class)
                .clickArchivedCheckbox()
                .checkCategoryInCategoryList(user.testData().categories().getFirst().name());
    }

    @User(
            categories = @Category
    )
    @ApiLogin
    @DisplayName("Активная категория должна присутствовать и отображаться в списке категорий")
    @Test
    void activeCategoryShouldPresentInCategoriesList(@NotNull UserJson user) {
        Selenide.open(ProfilePage.PROFILE_PAGE_URL, ProfilePage.class)
                .checkCategoryInCategoryList(user.testData().categories().getFirst().name());
    }

    @User
    @ApiLogin
    @DisplayName("Обновление всех полей профиля")
    @Test
    void updateAllFieldsProfile() {
        Selenide.open(ProfilePage.PROFILE_PAGE_URL, ProfilePage.class)
                .uploadImage("image/duck.png")
                .setName(RandomDataUtils.randomName())
                .setNewCategory(RandomDataUtils.randomCategoryName())
                .saveChanges()
                .checkAlertMessage("Profile successfully updated");
    }

    @DisplayName("Проверка корректной загрузки аватарки")
    @User
    @ApiLogin
    @ScreenShotTest(value = "image/expected-avatar.png")
    void checkCorrectUploadAvatar(BufferedImage expected) throws IOException {
        Selenide.open(ProfilePage.PROFILE_PAGE_URL, ProfilePage.class)
                .uploadImage("image/duck.png")
                .saveChanges()
                .checkPhoto(expected);
    }
}
