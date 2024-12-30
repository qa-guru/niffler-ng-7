package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage {
    private final SelenideElement header = $(By.xpath("//*[contains(text(), 'Profile')]"));
    private final SelenideElement profilePicture = $(By.xpath("//*[data-testid = 'PersonIcon']"));
    private final SelenideElement nifflerLogo = $(By.xpath("//*[@href='/main']"));
    public ProfilePage clickLogo(){
        nifflerLogo.click();
        return new ProfilePage();
    }

    private final SelenideElement newSpendingBtn = $(By.xpath("//*[@href='/spending']"));

    public ProfilePage clickNewSpendingBtn(){
        newSpendingBtn.click();
        return new ProfilePage();
    }


    private final SelenideElement userNameField = $(By.xpath("//*[@id='username']"));
    public ProfilePage writeUserNameField(String userName){
        userNameField.clear();
        userNameField.setValue(userName);
        return new ProfilePage();
    }

    private final SelenideElement nameField = $(By.xpath("//*[@id='name']"));
    public ProfilePage writeNameField(String nameValue){
        nameField.clear();
        nameField.setValue(nameValue);
        return new ProfilePage();
    }

    private final SelenideElement saveChangesBtn = $(By.xpath("//*[@id=':r1:']"));
    public ProfilePage writePasswordSubmitField(String passwordSubmitValue){
        saveChangesBtn.click();
        return new ProfilePage();
    }

    private final SelenideElement categoryField = $(By.xpath("//*[@id='category']"));
    public ProfilePage setCategoryField(String category){
        categoryField.setValue(category).pressEnter();
        return new ProfilePage();
    }

    private final SelenideElement showArchiveBtn = $(By.xpath("//*[contains(text(), 'Show archived')]"));
    public ProfilePage clickShowArchiveBtnField(){
        showArchiveBtn.click();
        return new ProfilePage();
    }

    private final SelenideElement editCategoryBtn = $(By.xpath("//*[@aria-label='Edit category']"));
    public ProfilePage editCategoryBtn(){
        editCategoryBtn.click();
        return new ProfilePage();
    }

    private final SelenideElement archivedCategoryBtn = $(By.xpath("//*[@aria-label='Archive category']"));
    public ProfilePage archivedCategoryBtnShouldBeVisible(){
        archivedCategoryBtn.shouldBe(visible);
        return new ProfilePage();
    }

    private final SelenideElement unarchivedCategoryBtn = $(By.xpath("//*[@data-testid='UnarchiveOutlinedIcon']"));
    public ProfilePage unarchivedCategoryBtn(){
        unarchivedCategoryBtn.click();
        return new ProfilePage();
    }

    private final SelenideElement archiveAcceptBtn = $(By.xpath("//*[contains(text(), 'Archive')]"));
    public ProfilePage archiveAcceptBtn(){
        archiveAcceptBtn.click();
        return new ProfilePage();
    }

    private final SelenideElement unarchivedAcceptBtn = $(By.xpath("////*[contains(text(), 'Unarchive')]"));
    public ProfilePage unarchivedAcceptBtn(){
        unarchivedAcceptBtn.click();
        return new ProfilePage();
    }

    private final SelenideElement closeArchiveAcceptBtn = $(By.xpath("//*[@type='submit']"));
    public ProfilePage closeArchiveAcceptBtn(){
        closeArchiveAcceptBtn.click();
        return new ProfilePage();
    }

    private final SelenideElement activeCategory = $(By.xpath("//*[contains(@class, 'css-gq8o4k')]"));
    public ProfilePage activeCategoryShouldBeVisible(){
        activeCategory.shouldBe(visible);
        return new ProfilePage();
    }

    private final SelenideElement archiveCategory = $(By.xpath("//*[contains(@class, 'css-1thst09')]"));
    public ProfilePage archiveCategoryShouldBeVisible(){
        archiveCategory.shouldBe(visible);
        return new ProfilePage();
    }

}
