package guru.qa.niffler.helpers.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;

public class BrowserExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        TestExecutionExceptionHandler,
        LifecycleMethodExecutionExceptionHandler {

    private static void deScreenshot() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Allure.addAttachment("Screen on fail",
                    new ByteArrayInputStream(
                            ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES)));
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Selenide.closeWebDriver();
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        SelenideLogger.addListener("Allure-selenide", new AllureSelenide()
                .savePageSource(false)
                .screenshots(false));
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, @NotNull Throwable throwable) throws Throwable {
        deScreenshot();
        throw throwable;

    }

    @Override
    public void handleBeforeEachMethodExecutionException(ExtensionContext context, @NotNull Throwable throwable) throws Throwable {
        deScreenshot();
        throw throwable;
    }

    @Override
    public void handleAfterEachMethodExecutionException(ExtensionContext context, @NotNull Throwable throwable) throws Throwable {
        deScreenshot();
        throw throwable;
    }
}
