package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;

import static guru.qa.niffler.utils.RandomDataUtils.randomSentence;
import static guru.qa.niffler.utils.ScreenDiffResult.checkActualImageEqualsExpected;

@WebTest

public class SpendingWebTest {

    private static final Config CFG = Config.getInstance();

    @User(
            spendings = @Spending(
                    category = "Food",
                    description = "apples",
                    amount = 50
            )
    )
    @Test
    void categoryDescriptionShouldBeChangedFromTable(UserJson user) {
        final String newDescription = "bananas";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), "123")
                .editSpending("apples")
                .setDescription(newDescription)
                .save()
                .checkAlertMessage("Spending is edited successfully");
        ;

        new MainPage().checkThatTableContainsSpending(newDescription);
    }

    @User
    @Test
    void shouldAddNewSpending(UserJson user) {
        String category = "Food";
        int amount = 100;
        Date currentDate = new Date();
        String description = randomSentence(1);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .checkIsLoaded()
                .getHeader()
                .addSpendingPage()
                .setSpendingAmount(amount)
                .setCategory(category)
                .setSpendingDate(currentDate)
                .setDescription(description)
                .save()
                .checkAlertMessage("New spending is successfully created");

        new MainPage().getSpendingTable()
                .checkTableContains(description);
    }


    @User(
            spendings = @Spending(
                    category = "Food",
                    description = "apples",
                    amount = 50
            )
    )

    @ScreenShotTest("img/expected-stat.png")
    void checkStatComponentTest(UserJson user, BufferedImage expected) throws IOException {

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), "123");

        checkActualImageEqualsExpected(expected, "canvas[role='img']");
    }


    @User(
            spendings = {
                    @Spending(
                            category = "Food",
                            description = "apples",
                            amount = 50
                    ),
                    @Spending(
                            category = "Animal",
                            description = "cat",
                            amount = 350
                    )
            }
    )
    @ScreenShotTest("img/spend.png")
    void checkStatComponentAfterDeleteSpending(UserJson user, BufferedImage expected) throws IOException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), "123")
                .getStatComponent()
                .checkStatisticBubblesContains("Animal 350", "Food 50");
        checkActualImageEqualsExpected(expected, "canvas[role='img']");

        new MainPage().getSpendingTable()
                .deleteSpending("cat");

        new MainPage()
                .getStatComponent()
                .checkStatisticBubblesContains("Food 50");

        expected = ImageIO.read(
                new ClassPathResource(
                        "img/expected-stat.png"
                ).getInputStream());

        checkActualImageEqualsExpected(expected, "canvas[role='img']");
    }

    @User(
            spendings = {
                    @Spending(
                            category = "Food",
                            description = "apples",
                            amount = 50
                    ),
                    @Spending(
                            category = "Animal",
                            description = "cat",
                            amount = 350
                    )
            }
    )
    @ScreenShotTest("img/spend.png")
    void checkStatComponentAfterEditSpending(UserJson user, BufferedImage expected) throws IOException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), "123")
                .getStatComponent()
                .checkStatisticBubblesContains("Animal 350", "Food 50");
        checkActualImageEqualsExpected(expected, "canvas[role='img']");

        new MainPage().getSpendingTable()
                .editSpending("apples")
                .setSpendingAmount(1000)
                .save();
        new MainPage().getStatComponent()
                .checkStatisticBubblesContains("Food 1000", "Animal 350");
        ;

        expected = ImageIO.read(
                new ClassPathResource(
                        "img/spend-after-edit.png"
                ).getInputStream());

        checkActualImageEqualsExpected(expected, "canvas[role='img']");
    }
}
