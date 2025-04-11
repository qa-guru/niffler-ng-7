package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.jupiter.extension.ScreenShotTestExtension;
import guru.qa.niffler.model.Bubble;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.StatConditions.statBubble;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ParametersAreNonnullByDefault
public class StatComponent extends BaseComponent<StatComponent> {

  public StatComponent() {
    super($("#stat"));
  }

  private final ElementsCollection bubbles = self.$("#legend-container").$$("li");
  private final SelenideElement chart = $("canvas[role='img']");

  @Step("Check that statistic image matches the expected image")
  @Nonnull
  public StatComponent checkStatisticImage(BufferedImage expectedImage) throws IOException {
    Selenide.sleep(2000);
    assertFalse(
        new ScreenDiffResult(
            chartScreenshot(),
            expectedImage
        ),
        ScreenShotTestExtension.ASSERT_SCREEN_MESSAGE
    );
    return this;
  }

  @Step("Get screenshot of stat chart")
  @Nonnull
  public BufferedImage chartScreenshot() throws IOException {
    return ImageIO.read(requireNonNull(chart.screenshot()));
  }

  @Step("Check that stat bubbles contains expected colors & texts {expected}")
  @Nonnull
  public StatComponent checkBubbles(Bubble... expected) {
    bubbles.should(statBubble(expected));
    return this;
  }
}
