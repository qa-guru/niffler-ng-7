package guru.qa.niffler.utils;

import guru.qa.niffler.jupiter.extension.ScreenShotTestExtension;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.function.BooleanSupplier;

import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ScreenDiffResult implements BooleanSupplier {
    private final BufferedImage expected;
    private final BufferedImage actuall;
    private final ImageDiff diff;
    private final boolean hasDiff;

    public ScreenDiffResult(BufferedImage expected, BufferedImage actuall) {
        this.expected = expected;
        this.actuall = actuall;
        this.diff= new ImageDiffer().makeDiff(expected, actuall);
        this.hasDiff=diff.hasDiff();
    }

    @Override
    public boolean getAsBoolean() {
        if (hasDiff){
            ScreenShotTestExtension.setExpected(expected);
            ScreenShotTestExtension.setActual(actuall);
            ScreenShotTestExtension.setDiff(diff.getMarkedImage());
        }
        return false;
    }

    public static void checkActualImageEqualsExpected(BufferedImage expected, String actualPath) throws IOException {
        BufferedImage actual = ImageIO.read(Objects.requireNonNull($(actualPath).screenshot()));
        assertFalse(new ScreenDiffResult(
                expected,
                actual
        ));
    }
}
