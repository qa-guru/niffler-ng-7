package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class PeoplesPage extends BasePage<PeoplesPage>{

    public static final String URL = CFG.frontUrl() + "people/all";

    private final SelenideElement
            searchInput = $("input[aria-label='search']"),
            peopleTab = $("a[href='/people/friends']"),
            allTab = $("a[href='/people/all']");

    @Override
    @Nonnull
    public PeoplesPage checkThatPageLoaded() {
        peopleTab.shouldBe(visible);
        allTab.shouldBe(visible);
        return this;
    }

    @Step("Проверяем, что '{0}' присутствует в списке исходящих друзей")
    @Nonnull
    public PeoplesPage checkOutcomeRequest(String username) {
        searchInput.setValue(username).pressEnter();
        $("#all").$$("tr").find(text(username)).$("span").shouldHave(text("Waiting..."));
        return this;
    }
}