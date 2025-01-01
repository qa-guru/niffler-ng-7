package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import guru.qa.niffler.data.SpendData;
import guru.qa.niffler.element.Button;
import guru.qa.niffler.element.Input;
import guru.qa.niffler.element.Table;
import guru.qa.niffler.element.TextField;
import io.qameta.allure.Step;

import org.openqa.selenium.Keys;


import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

public class MainPage extends LoginPage {
    private final TextField headerText = new TextField("Текст история трат",
            $(".MuiTypography-root.MuiTypography-h5.css-uxhuts"));
    private final Input searchInput = new Input("Input поиска трат",
            $(".MuiInputBase-input.css-mnn31"));
    private final Table resultTable = new Table("Таблица результат поиска",
            $("tbody"));
    private final Button delButton = new Button("Кнопка удаления",
            $("#delete"));
    private final Button acceptDelButton = new Button("Кнопка подтверждения удаления",
            $x("(//button[text()='Delete'])[2]"));
    private final SelenideElement statistic = $("#stat");
    private final SelenideElement spendingBlock = $("#spendings");
    private final SelenideElement openUserProfileDropDownButton =
            $x("//div[contains(@class,'MuiAvatar-root MuiAvatar-circular')]");
    private final SelenideElement openUserProfileButton = $(".link.nav-link");
    private final SelenideElement friendsButton = $x("(//A[@class='link nav-link'])[2]");

    @Step("Переходим в раздел друзья")
    public MainPage moveToFriendsPage(){
        openUserProfileDropDownButton.click();
        friendsButton.click();
        return this;
    }

    @Step("Переходим в профиль пользователя")
    public MainPage moveToUserProfile(){
        openUserProfileDropDownButton.click();
        openUserProfileButton.click();
        return this;
    }

    @Step("Проверяем загрузку элементов Статистика и История трат после успешной авторизации")
    public MainPage pageLoadCheck() {
        assertAll("Main page load",
                () -> assertTrue(statistic.isEnabled(),
                        "Блок статистики не отобразился"),
                () -> assertTrue(spendingBlock.isEnabled(),
                        "Блок история трат не отображется"));
        return this;

    }

    @Step("Проверяем значение текстового поля История трат")
    public MainPage historyOfSpendingTextCheck(String value) {
        assertEquals(value, headerText.getText(),
                "Текстовое поле History of Spending заполнено некорректно");
        return this;
    }

    @Step("Ищем трату по Category")
    public MainPage searchSpend(String value) {
        searchInput.setValue(value);
        actions().sendKeys(Keys.ENTER).build().perform();
        return this;
    }

    @Step("Нажимаем кнопку редактировать")
    public MainPage editButtonClick() {
        resultTable.getElementFromCell(0, 5).click();
        return this;
    }

    @Step("Проверяем заполнение таблицы")
    public MainPage resultTableCheck(SpendData.spend[] data) {
        List<SpendData.spend> d = List.of(data);
        for (int i = 0; i < d.size(); i++) {
            assertEquals(d.get(i).getValue(), resultTable.getValueFromCell(1, i + 2),
                    "Изменённая трата не отображается в таблице");
        }
        return this;
    }

    @Step("Устанавливаем чек бокс для выбора траты")
    public MainPage chooseSpend() {
        resultTable.getElementFromCell(0, 0).click();
        return this;
    }

    @Step("Нажимаем кнопку удалить")
    public MainPage delButtonClick() {
        delButton.buttonClick();
        return this;
    }

    @Step("Нажимаем кнопку подтверждения удаления")
    public MainPage acceptDelButtonClick() {
        acceptDelButton.buttonClick();
        return this;
    }

    @Step("Проверка что таблица не отображается")
    public MainPage resultTableShouldBeNotVisibleCheck() {
        assertFalse(resultTable.visibilityCheck(), "Трата не удалилась");
        return this;
    }
}
