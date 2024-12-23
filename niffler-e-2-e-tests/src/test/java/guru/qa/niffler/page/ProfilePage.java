package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;

import static com.codeborne.selenide.Selenide.$x;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProfilePage {

    @NotNull
    private SelenideElement activeCategory(String categoryName) {
        return $x("//SPAN[@class='MuiChip-label MuiChip-labelMedium css-14vsv3w'][text()='" + categoryName + "']");
    }
    @NotNull
    private SelenideElement makeCategoryArchivedButton(String categoryName) {
        return $x("//span[text()='" + categoryName + "']/../..//button[@aria-label=\"Archive category\"]");
    }
    private final SelenideElement confirmArchivingButton = $x("//button[text()='Archive']");
    private final SelenideElement showArchivedToggleSwitch = $x("//span[text()='Show archived']");


    @Step("Переключаем toggleSwitch для доступности архивных категорий")
    public ProfilePage showArchivedToggleSwitchClick(){
        showArchivedToggleSwitch.click();
        return this;
    }

    @Step("Проверяем недоступность архивной категории")
    public ProfilePage archivedCategoryDoNotVisibleInActiveCategoryTableCheck(String categoryName){
        activeCategory(categoryName).shouldNotBe(Condition.visible);
        return this;
    }


    @Step("Переводим категорию в архив")
    public ProfilePage makeCategoryArchived(String categoryName){
        makeCategoryArchivedButton(categoryName).click();
        confirmArchivingButton.click();
        return this;
    }

    @Step("Проверяем доступность активной категории")
    public ProfilePage activeCategoryVisibleCheck(String categoryName) {
        try {
            assertTrue(activeCategory(categoryName).isEnabled(),
                    "Активная категория не отображается");
        } catch (ElementNotFound e) {
            throw new AssertionError("Элемент не найден на странице");
        }
        return this;
    }
}
