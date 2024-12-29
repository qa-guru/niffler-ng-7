package guru.qa.niffler.page.components;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.page.ProfilePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class NavigateMenuComponent {
    private final SelenideElement
            accountMenuButton = $(".css-1obba8g"),
            profileButton = $("a[href='/profile']"),
            friendsButton = $("a[href='/people/friends']"),
            allPeopleButton = $("a[href='/people/all']");

    @Step("Нажатие на меню аккаунта ")
    public NavigateMenuComponent clickAccountMenuButton() {
        accountMenuButton.click();
        return this;
    }

    @Step("Нажатие на кнопку 'Профиль'")
    public ProfilePage clickProfileButton() {
        profileButton.click();
        return new ProfilePage();
    }

    @Step("Нажатие на кнопку 'Друзья'")
    public FriendsPage clickFriendsButton() {
        friendsButton.click();
        return new FriendsPage();
    }

    @Step("Нажатие на кнопку 'Все люди'")
    public PeoplePage clickAllPeopleButton() {
        allPeopleButton.click();
        return new PeoplePage();
    }
}
