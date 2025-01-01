package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.helpers.jupiter.annotation.UserType;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class FriendsPage {

    protected final SelenideElement emptyTableText = $(".MuiTypography-root.MuiTypography-h6.css-1m7obeg");
    protected final SelenideElement friendName = $(".MuiTypography-root.MuiTypography-body1.css-8va9ha");
    protected final SelenideElement acceptButton = $x("(//button[@type='button'])[2]");
    protected final SelenideElement switchToAllPeopleTable = $x("//h2[text()='All people']");
    protected final SelenideElement waitingLiable = $x("//span[text()='Waiting...']");

    @Step("Переключаем таблицу на кладку All people")
    public FriendsPage switchForAllPeopleTable(){
        switchToAllPeopleTable.click();
        return this;
    }

    @Step("Проверяем результат в талице")
    public FriendsPage tableCheck(String value, UserType.Type usertype) {
        switch (usertype) {
            case EMPTY:
                emptyTableText.shouldBe(visible.because("Таблица не пустая"));
                emptyTableText.should(text(value).because("Текст при пустой талице некорректный"));
                break;
            case WITH_FRIEND:
                emptyTableText.shouldNotBe(visible.because("Таблица не пустая"));
                friendName.should(text(value).because("Имя друга указано некорректно"));
                break;
            case WITH_SEND_REQUEST:
                friendName.should(text(value).because("Имя друга указано некорректно"));
                acceptButton.shouldBe(clickable.because("Кнопка принять не кликабельна"));
                break;
            case WITH_GET_REQUEST:
                waitingLiable.shouldBe(visible);
                break;
            default:
                throw new AssertionError("В проверке таблице выбран некорректный тип пользователя");
        }

        return this;
    }

}
