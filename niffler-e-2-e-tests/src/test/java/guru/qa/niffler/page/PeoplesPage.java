package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class PeoplesPage {

    public PeoplesPage checkOutcomeRequest(String username) {
        $("#all").$$("tr").find(text(username)).$("span").shouldHave(text("Waiting..."));
        return this;
    }
}