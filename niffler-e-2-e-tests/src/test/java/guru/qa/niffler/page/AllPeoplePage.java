package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

public class AllPeoplePage {
    private final ElementsCollection allPeopleTableRows = $$("#all tr");

    public void shouldSeeOutcomeInvitationInAllPeoplesTable(String invitationFriendName) {
        allPeopleTableRows.find(text(invitationFriendName)).should(visible);
        String expected = "Waiting...";
        allPeopleTableRows.find(text(invitationFriendName)).find("td", 1).shouldHave(text(expected));
    }
}
