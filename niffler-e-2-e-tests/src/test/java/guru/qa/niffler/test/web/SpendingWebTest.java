package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.condition.Bubble;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
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
import static guru.qa.niffler.utils.ScreenDiffResult.checkActualImageEqualsExpected;
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
    void checkStatComponentTest(UserJson user, BufferedImage expected) throws InterruptedException, IOException {

        StatComponent statComponent = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), "123")
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
    @ScreenShotTest("img/spend.png")
    void checkStatComponentAfterDeleteSpending(UserJson user, BufferedImage expected) throws IOException {
        StatComponent statComponent = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), "123")
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
    @ScreenShotTest("img/spend.png")
    void checkStatComponentAfterEditSpending(UserJson user, BufferedImage expected) throws IOException {
        StatComponent statComponent = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), "123")
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
    @Test
    void checkSpendsTest(UserJson user) {
        List<SpendJson> expectedSpends = user.testData().spendings();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password());

        new MainPage()
                .getSpendingTable()
                .checkSpendTable(expectedSpends);
    }
}
