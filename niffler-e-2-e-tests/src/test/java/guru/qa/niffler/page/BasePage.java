package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage <T extends BasePage<?>> {

    private final SelenideElement alert = $(".MuiAlert-message");

    public T checkPageLoaded(String text){
        alert.should(text(text));
        return (T) this;
    };
}
