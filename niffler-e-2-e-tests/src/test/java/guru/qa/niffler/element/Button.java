package guru.qa.niffler.element;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Button {
    public Button(String name, SelenideElement selector) {
        this.name = name;
        this.selector = selector;
    }

    private String name;
    private SelenideElement selector;

    public void buttonClick(){
        selector.shouldBe(Condition.visible);
        selector.click();
    }
}
