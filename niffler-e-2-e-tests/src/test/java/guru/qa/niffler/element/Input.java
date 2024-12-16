package guru.qa.niffler.element;


import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;


import static org.junit.jupiter.api.Assertions.*;

public class Input {

    private final String name;
    private final SelenideElement selector;

    public Input(String name, SelenideElement selector) {
        this.name = name;
        this.selector = selector;
    }

    @Step("Вводим значение {value}")
    public  void setValue(String value){
        assertTrue(selector.isDisplayed(),"Input "+name+ " недоступен в течении 3 секунд");
        selector.setValue(value);
    }

}
