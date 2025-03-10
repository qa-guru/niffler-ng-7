package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.SpendJson;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.CheckResult.rejected;

public class SpendConditions {

    public static WebElementsCondition expectedSpends(List<SpendJson> expectedSpends) {
        return new WebElementsCondition() {

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> rows) {
                if (expectedSpends.isEmpty()) {
                    throw new IllegalArgumentException("No expected spends given");
                }

                if (expectedSpends.size() != rows.size()) {
                    final String message = String.format(
                            "List size mismatch (expected: %s, actual: %s)", expectedSpends.size(), rows.size()
                    );
                    return rejected(message, rows.toString());
                }


//                if (expectedSpends.size() != rows.size()) {
//                    final String message = String.format("List bubbles mismatch (expected : %s, actual: %s)", expectedSpends.size(), rows.size());
//                    return CheckResult.rejected(message, rows);
//                }

                for (int i = 0; i < rows.size(); i++) {
                    SpendJson expectedSpend = expectedSpends.get(i);
                    WebElement spendingRow = rows.get(i);
                    List<WebElement> cells = spendingRow.findElements(By.tagName("td"));

                    String actualCategory = cells.get(1).getText();
                    String actualAmount = cells.get(2).getText();
                    String actualDescription = cells.get(3).getText();
                    String actualDate = cells.get(4).getText();

                    String expectedCategory = expectedSpend.category().name();
                    String expectedAmount = expectedSpend.amount() + " " + expectedSpend.currency().value;
                    String expectedDescription = expectedSpend.description();
                    String expectedDate = convertDate(expectedSpend.spendDate());


                    System.out.println("!!" + expectedCategory);
                    System.out.println("!!" + actualCategory);

                    System.out.println("!!!!" + expectedAmount);
                    System.out.println("!!!!" + actualAmount);

                    System.out.println("!!!!" + expectedDescription);
                    System.out.println("!!!!" + actualDescription);

                    System.out.println("!!!!" + expectedDate);
                    System.out.println("!!!!" + actualDate);


                    boolean passed;

                    passed = actualCategory.equals(expectedCategory)
                            && actualAmount.equals(expectedAmount)
                            && actualDescription.equals(expectedDescription)
                            && actualDate.equals(expectedDate);


                    if (!passed) {
                        final String message = String.format("List color or text mismatch (expected : %s, %s, %s, %s actual: %s, %s, %s, %s)",
                                expectedCategory, expectedAmount, expectedDescription, expectedDate, actualCategory, actualAmount, actualDescription, actualDate);
                        return rejected(message, rows);
                    }
                }
                return CheckResult.accepted();
            }

            @Override
            public String toString() {
                return List.of(expectedSpends).toString();
            }
        };
    }

    private static String convertDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        return formatter.format(date);
    }
}
