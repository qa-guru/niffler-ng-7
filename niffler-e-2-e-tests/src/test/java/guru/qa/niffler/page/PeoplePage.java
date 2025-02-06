package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.components.SearchField;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class PeoplePage {

    private final SearchField searchField = new SearchField();

    private final SelenideElement allPeopleTable = $("#all");

    public void checkOutcomeFriendRequest(String outcomeUserName) {
        searchField.search(outcomeUserName);
        allPeopleTable.$$("tr").findBy(text(outcomeUserName))
                .shouldHave(text("Waiting..."));

    }
}
