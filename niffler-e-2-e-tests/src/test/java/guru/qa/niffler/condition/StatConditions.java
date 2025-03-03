package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.WebElementsCondition;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatConditions {

    public static WebElementCondition color(Color expectrdColor) {
        return new WebElementCondition("color") {
            @NotNull
            @Override
            public CheckResult check(Driver driver, WebElement webElement) {
                final String rgba = webElement.getCssValue("background-color");
                return new CheckResult(
                        expectrdColor.rgb.equals(rgba),
                        rgba
                );
            }
        };
    }

    public static WebElementsCondition color(Color... colors) {
        return new WebElementsCondition() {
            private final String expectedRgba = Arrays.stream(colors).map(c -> c.rgb).toList().toString();

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(colors)) {
                    throw new IllegalArgumentException("No expected colors given");
                }

                if (colors.length != elements.size()) {
                    final String message = String.format("List colors mismatch (expected : %s, actua l: %s)", colors.length, elements.size());
                    return CheckResult.rejected(message, elements);
                }

                boolean passed = true;
                List<String> actualRgbaList = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    final WebElement elementToCheck = elements.get(i);
                    final Color colorToCheck = colors[i];
                    final String rgba = elementToCheck.getCssValue("background-color");
                    actualRgbaList.add(rgba);

                    if (passed) {
                        passed = colorToCheck.rgb.equals(rgba);
                    }
                }
                if (!passed){
                    final String actualRgba = actualRgbaList.toString();
                    final String message = String.format("List size mismatch (expected : %s, actua l: %s)", expectedRgba, actualRgba);
                    return CheckResult.rejected(message, actualRgba);
                }
                return CheckResult.accepted();
            }

            @Override
            public String toString() {
                return expectedRgba;
            }
        };
    }
}