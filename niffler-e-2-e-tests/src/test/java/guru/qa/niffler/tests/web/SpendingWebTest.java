package guru.qa.niffler.tests.web;

import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import guru.qa.niffler.utils.ScreenDiffResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertFalse;

@WebTest
public class SpendingWebTest {


    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @DisabledByIssue("5")
    @DisplayName("Название категории должно быть изменено в таблице")
    @Test
    void categoryDescriptionShouldBeChangedFromTable(UserJson user) {
        final String newDescription = "Обучение Niffler Next Generation";

        new LoginPage()
                .open()
                .login(user.username(), user.testData().password())
                .editSpendingClick(user.testData().spends().getFirst().description())
                .editDescription(newDescription)
                .saveChange()
                .checkAlertMessage("Spending is edited successfully")
                .checkThatTableContainsSpending(newDescription);
    }

    @User
    @DisplayName("Должна быть добавлена новая трата")
    @Test
    void shouldAddNewSpending(UserJson user) {
        String category = "Duck";
        int amount = 100;
        LocalDate currentDate = LocalDate.now();
        String description = RandomDataUtils.randomSentence(1);

        new LoginPage()
                .open()
                .login(user.username(), user.testData().password())
                .getHeader()
                .addSpendingPage()
                .editCategory(category)
                .editAmount(amount)
                .editDate(currentDate)
                .editDescription(description)
                .saveChange()
                .checkAlertMessage("New spending is successfully created")
                .getSpendingTable()
                .checkTableContains(description);
    }

    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @DisplayName("Проверка компонента статистики")
    @ScreenShotTest("img/expected-stat.png")
    void checkStatComponentTest(UserJson user, BufferedImage expected) throws IOException {
        new LoginPage()
                .open()
                .login(user.username(), user.testData().password());
        BufferedImage actual = ImageIO.read(Objects.requireNonNull($("canvas[role='img']").screenshot()));
        assertFalse(new ScreenDiffResult(
                expected,
                actual
        ));
    }
}

