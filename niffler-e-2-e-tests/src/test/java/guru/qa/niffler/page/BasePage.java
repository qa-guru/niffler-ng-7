package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public abstract class BasePage<T extends BasePage<?>> {
    protected static final Config CFG = Config.getInstance();

    private final SelenideElement alert;

    protected BasePage(SelenideDriver driver) {
        this.alert = driver.$(".MuiSnackbar-root");
    }

    public BasePage() {
        this.alert = Selenide.$(".MuiSnackbar-root");
    }

    public abstract T checkThatPageLoaded();

    @Step("Проверка, что alert содержит текст {text}")
    @SuppressWarnings("unchecked")
    @Nonnull
    public T checkAlertMessage(String text) {
        alert.should(visible);
        alert.should(text(text));
        return (T) this;
    }
}
