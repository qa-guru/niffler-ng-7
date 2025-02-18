package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class SearchField {

    private final SelenideElement
            searchInput = $("input[aria-label='search']"),
            clearSearchInputBtn = $("#input-clear");

    @Step("Вводим '{0}' в поисковик")
    @Nonnull
    public SearchField search(String query){
        clearIfNotEmpty();
        searchInput.setValue(query).pressEnter();
        return this;
    }

    @Nonnull
    public SearchField clearIfNotEmpty(){
        if (searchInput.is(not(empty))){
            clearSearchInputBtn.click();
            searchInput.shouldBe(empty);
        }
        return this;
    }
}
