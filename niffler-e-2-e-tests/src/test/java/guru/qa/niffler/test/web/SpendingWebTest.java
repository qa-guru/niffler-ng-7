package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.condition.Bubble;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.component.StatComponent;
import guru.qa.niffler.utils.ScreenDiffResult;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static guru.qa.niffler.utils.RandomDataUtils.randomSentence;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
    @ApiLogin
    @Test
    void categoryDescriptionShouldBeChangedFromTable() {
        final String newDescription = "bananas";

        Selenide.open(MainPage.URL, MainPage.class)
                .checkThatPageLoaded()
                .editSpending("apples")
                .setDescription(newDescription)
                .save();

        new MainPage()
                .checkThatPageLoaded()
                .checkThatTableContainsSpending(newDescription);
    }

    @User
    @ApiLogin
    @Test
    void shouldAddNewSpending() {
        String category = "Food";
        int amount = 100;
        Date currentDate = new Date();
        String description = randomSentence(1);

        Selenide.open(MainPage.URL, MainPage.class)
                .checkIsLoaded()
                .getHeader()
                .addSpendingPage()
                .setSpendingAmount(amount)
                .setCategory(category)
                .setSpendingDate(currentDate)
                .setDescription(description)
                .save();

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
    @ApiLogin
    @ScreenShotTest(expected = "img/expected-stat.png")
    void checkStatComponentTest(BufferedImage expected) throws InterruptedException, IOException {
        StatComponent statComponent = Selenide.open(MainPage.URL, MainPage.class)
                .getStatComponent();

        Thread.sleep(3000);
        assertFalse(new ScreenDiffResult(
                expected,
                statComponent.chartScreenshot()
        ), "Screen comparison failure");

        statComponent.checkBubbles(new Bubble(Color.YELLOW, "Food 50 ₽"));
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
    @ApiLogin
    @ScreenShotTest(expected = "img/spend.png")
    void checkStatComponentAfterDeleteSpending(BufferedImage expected) throws IOException {
        StatComponent statComponent = Selenide.open(MainPage.URL, MainPage.class)
                .getStatComponent();

        assertFalse(new ScreenDiffResult(
                expected,
                statComponent.chartScreenshot()
        ), "Screen comparison failure");

        statComponent.checkBubbles(new Bubble(Color.YELLOW, "Animal 350 ₽"),
                new Bubble(Color.GREEN, "Food 50 ₽"));

        new MainPage().getSpendingTable()
                .deleteSpending("cat");

        statComponent.checkBubbles(new Bubble(Color.YELLOW, "Food 50 ₽"));

        expected = ImageIO.read(
                new ClassPathResource(
                        "img/expected-stat.png"
                ).getInputStream());

        assertFalse(new ScreenDiffResult(
                expected,
                statComponent.chartScreenshot()
        ), "Screen comparison failure");
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
    @ApiLogin
    @ScreenShotTest(expected = "img/spend.png")
    void checkStatComponentAfterEditSpending(BufferedImage expected) throws IOException {
        StatComponent statComponent = Selenide.open(MainPage.URL, MainPage.class)
                .getStatComponent();

        assertFalse(new ScreenDiffResult(
                expected,
                statComponent.chartScreenshot()
        ), "Screen comparison failure");

        statComponent.checkBubbles(new Bubble(Color.GREEN, "Food 50 ₽"));

        new MainPage().getSpendingTable()
                .editSpending("apples")
                .setSpendingAmount(1000)
                .save();

        statComponent.checkBubbles(new Bubble(Color.YELLOW, "Food 1000 ₽"),
                new Bubble(Color.GREEN, "Animal 350 ₽"));

        expected = ImageIO.read(
                new ClassPathResource(
                        "img/spend-after-edit.png"
                ).getInputStream());

        assertFalse(new ScreenDiffResult(
                expected,
                statComponent.chartScreenshot()
        ), "Screen comparison failure");
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
    @ApiLogin
    @Test
    void checkSpendsTest(UserJson user) {
        List<SpendJson> expectedSpends = user.testData().spendings();

        Selenide.open(MainPage.URL, MainPage.class)
                .checkThatPageLoaded().getSpendingTable()
                .checkSpendTable(expectedSpends);
    }
}
