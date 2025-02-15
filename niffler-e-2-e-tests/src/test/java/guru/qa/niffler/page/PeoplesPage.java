package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class PeoplesPage {

    private final SelenideElement
            searchInput = $("input[aria-label='search']");

    public PeoplesPage checkOutcomeRequest(String username) {
        searchInput.setValue(username).pressEnter();
        $("#all").$$("tr").find(text(username)).$("span").shouldHave(text("Waiting..."));
        return this;
    }
}