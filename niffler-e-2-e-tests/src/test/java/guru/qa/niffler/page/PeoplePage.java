package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class PeoplePage {
    private final SelenideElement
            allPeopleTable = $("#all"),
            searchField = $("input[placeholder='Search']");

    public void checkOutcomeFriendRequest(String outcomeUserName) {
        searchField.setValue(outcomeUserName).pressEnter();
        allPeopleTable.$$("tr").findBy(text(outcomeUserName))
                .shouldHave(text("Waiting..."));

    }
}
