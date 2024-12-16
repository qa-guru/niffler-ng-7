package guru.qa.niffler.element;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;
import java.util.function.BooleanSupplier;

public class TextField {
    public TextField(String name, SelenideElement selector) {
        this.name = name;
        this.selector = selector;
    }

    private String name;
    private SelenideElement selector;

    @Step("Получаем значение из текстового поля")
    public String getText(){
        selector.shouldNotBe(Condition.visible);
        return selector.getText();
    }
}
