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
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import static com.codeborne.selenide.Selenide.$;
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

        BufferedImage actual = ImageIO.read($("canvas[role='img']").screenshot());

        assertFalse(new ScreenDiffResult(
                expected,
                actual
        ));
    }



}
