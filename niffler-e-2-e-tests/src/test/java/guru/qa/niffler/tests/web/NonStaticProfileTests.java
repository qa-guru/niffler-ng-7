package guru.qa.niffler.tests.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.converter.Browser;
import guru.qa.niffler.jupiter.converter.BrowserConverter;
import guru.qa.niffler.jupiter.extension.NonStaticBrowsersExtension;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.EnumSource;

import java.awt.image.BufferedImage;
import java.io.IOException;

@DisplayName("Тесты для страницы профиля пользователя")
public class NonStaticProfileTests {

    @RegisterExtension
    private static final NonStaticBrowsersExtension nonStaticBrowsersExtension = new NonStaticBrowsersExtension();

    @User(
            username = "Artur",
            categories = @Category(archived = true)
    )
    @ParameterizedTest
    @EnumSource(Browser.class)
    @DisplayName("Архивная категория должна присутствовать и отображаться в списке категорий")
    void archivedCategoryShouldPresentInCategoriesList(@ConvertWith(BrowserConverter.class) SelenideDriver driver, @NotNull UserJson user) {
        nonStaticBrowsersExtension.drivers().add(driver);
        new LoginPage(driver)
                .open(driver)
                .login(user.username(), "12345", driver)
                .getHeader()
                .toProfilePage(driver)
                .clickArchivedCheckbox()
                .checkCategoryInCategoryList(user.testData().spends().getFirst().category().name(),driver);
    }

    @User(
            categories = @Category
    )
    @DisplayName("Активная категория должна присутствовать и отображаться в списке категорий")
    @Test
    void activeCategoryShouldPresentInCategoriesList(@NotNull UserJson user) {
        new LoginPage()
                .open()
                .login(user.testData().categories().getFirst().username(), user.testData().password())
                .getHeader()
                .toProfilePage()
                .checkCategoryInCategoryList(user.testData().categories().getFirst().name());
    }

    @User
    @DisplayName("Обновление всех полей профиля")
    @Test
    void updateAllFieldsProfile(@NotNull UserJson user) {
        new LoginPage()
                .open()
                .login(user.username(), user.testData().password())
                .getHeader()
                .toProfilePage()
                .uploadImage("image/duck.png")
                .setName(RandomDataUtils.randomName())
                .setNewCategory(RandomDataUtils.randomCategoryName())
                .saveChanges()
                .checkAlertMessage("Profile successfully updated");
    }

    @DisplayName("Проверка корректной загрузки аватарки")
    @User
    @ScreenShotTest(value = "image/expected-avatar.png")
    void checkCorrectUploadAvatar(@NotNull UserJson user, BufferedImage expected) throws IOException {
        new LoginPage()
                .open()
                .login(user.username(), user.testData().password())
                .getHeader()
                .toProfilePage()
                .uploadImage("image/duck.png")
                .saveChanges()
                .checkPhoto(expected);
    }
}
