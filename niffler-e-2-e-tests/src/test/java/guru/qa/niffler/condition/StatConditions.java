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
import java.util.Set;
import java.util.stream.Collectors;

public class StatConditions {

    public static WebElementCondition colorAndText(Color expectrdColor) {
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

    public static WebElementsCondition colorAndText(Bubble... bubble) {
        return new WebElementsCondition() {
            private final String expectedRgba = Arrays.stream(bubble).map(c -> c.color().rgb).toList().toString();
            private final String expectedText = Arrays.stream(bubble).map(Bubble::text).toList().toString();

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(bubble)) {
                    throw new IllegalArgumentException("No expected colors given");
                }

                if (bubble.length != elements.size()) {
                    final String message = String.format("List bubbles mismatch (expected : %s, actual: %s)", bubble.length, elements.size());
                    return CheckResult.rejected(message, elements);
                }

                boolean passed = true;
                List<String> actualRgbaList = new ArrayList<>();
                List<String> actualTextList = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    final WebElement elementToCheck = elements.get(i);
                    final Bubble colorToCheck = bubble[i];
                    final String rgba = elementToCheck.getCssValue("background-color");
                    final String text = elementToCheck.getText();
                    actualRgbaList.add(rgba);
                    actualTextList.add(text);

                    if (passed) {
                        passed = colorToCheck.color().rgb.equals(rgba) && colorToCheck.text().equals(text);
                    }
                }
                if (!passed) {
                    final String actualRgba = actualRgbaList.toString();
                    final String actualText = actualTextList.toString();
                    final String message = String.format("List color or text mismatch (expected : %s, %s, actual: %s, $s)",
                            expectedRgba, expectedText, actualRgba, actualText);
                    return CheckResult.rejected(message, actualRgba);
                }
                return CheckResult.accepted();
            }

            @Override
            public String toString() {
                return String.format(expectedRgba, expectedText);
            }
        };
    }

    public static WebElementsCondition colorAndTextInAnyOrder(Bubble... bubble) {
        return new WebElementsCondition() {
            private final Set<String> expectedRgba = Arrays.stream(bubble).map(c -> c.color().rgb).collect(Collectors.toSet());
            private final Set<String> expectedText = Arrays.stream(bubble).map(Bubble::text).collect(Collectors.toSet());

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(bubble)) {
                    throw new IllegalArgumentException("No expected colors given");
                }

                if (bubble.length != elements.size()) {
                    final String message = String.format("List bubbles mismatch (expected : %s, actual: %s)", bubble.length, elements.size());
                    return CheckResult.rejected(message, elements);
                }

                final Set<String> actualRgbaSet = elements.stream().map(e -> e.getCssValue("background-color"))
                        .collect(Collectors.toSet());

                final Set<String> actualTextSet = elements.stream().map(WebElement::getText)
                        .collect(Collectors.toSet());

                boolean colorMatch = actualRgbaSet.equals(expectedRgba);
                boolean textMatch = actualTextSet.equals(expectedText);

                if (!colorMatch || !textMatch) {
                    final String message = String.format("List color or text mismatch (expected : %s, %s, actual: %s, $s)",
                            expectedRgba, expectedText, actualRgbaSet, actualTextSet);
                    return CheckResult.rejected(message, elements);
                }
                return CheckResult.accepted();
            }

            @Override
            public String toString() {
                return String.format(String.valueOf(expectedRgba), expectedText);
            }
        };
    }


    public static WebElementsCondition containsColorAndText(Bubble... bubble) {
        return new WebElementsCondition() {
            private final Set<String> expectedRgba = Arrays.stream(bubble).map(c -> c.color().rgb).collect(Collectors.toSet());
            private final Set<String> expectedText = Arrays.stream(bubble).map(Bubble::text).collect(Collectors.toSet());

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                System.out.println("!!" + expectedRgba);
                System.out.println("!!" + expectedText);
                if (ArrayUtils.isEmpty(bubble)) {
                    throw new IllegalArgumentException("No expected colors given");
                }

                final Set<String> actualRgbaSet = elements.stream().map(e -> e.getCssValue("background-color"))
                        .collect(Collectors.toSet());

                final Set<String> actualTextSet = elements.stream().map(WebElement::getText)
                        .collect(Collectors.toSet());

                boolean colorMatch = actualRgbaSet.containsAll(expectedRgba);
                boolean textMatch = actualTextSet.containsAll(expectedText);

                if (!colorMatch || !textMatch) {
                    final String message = String.format("List color or text mismatch (expected : %s, %s, actual: %s, $s)",
                            expectedRgba, expectedText, actualRgbaSet, actualTextSet);
                    return CheckResult.rejected(message, elements);
                }
                return CheckResult.accepted();
            }

            @Override
            public String toString() {
                return String.format(String.valueOf(expectedRgba), expectedText);
            }
        };
    }

}