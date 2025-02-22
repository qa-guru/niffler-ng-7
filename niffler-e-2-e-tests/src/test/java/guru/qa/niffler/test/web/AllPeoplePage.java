package guru.qa.niffler.test.web;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class AllPeoplePage {
    private final ElementsCollection tabs = $$x(".//div[@role='tablist']/*");
    private final SelenideElement selectedTab = tabs.find(attributeMatching(
            "class",".*Mui-selected.*"));
    private final SelenideElement allPeopleTable = $x(".//table[./tbody[@id='all']]");

    public AllPeoplePage checkOutcomeInTable(String outcomeName) {
        String textInsteadButton = "Waiting...";
        allPeopleTable.$$x(".//tr").find(text(outcomeName)).shouldBe(allOf(visible,
                text(textInsteadButton)));
        return this;
    }

}
