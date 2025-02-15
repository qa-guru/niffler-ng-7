package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class Header {
    private final SelenideElement self = $("#root header");

    public void checkHeaderText(){
        self.$("h1").shouldHave(text("Niffler"));
    }
}
