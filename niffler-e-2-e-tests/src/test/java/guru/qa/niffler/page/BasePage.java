package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage<T extends BasePage<?>> {

    private final SelenideElement alert = $(".MuiAlert-message");

    @Step("Проверка, что alert содержит текст {text}")
    public T checkAlertMessage(String text) {
        alert.should(visible);
        alert.should(text(text));
        return (T) this;
    }
}
