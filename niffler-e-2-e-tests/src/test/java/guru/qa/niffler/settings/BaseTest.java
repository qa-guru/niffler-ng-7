package guru.qa.niffler.settings;

import com.codeborne.selenide.AssertionMode;
import com.codeborne.selenide.Selenide;

import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Configuration.*;

public class BaseTest {

    private static void setLocalDriver() {
        timeout = 300000;
        browser = "chrome";
        browserSize = "1920x1080";
        headless = false;
        assertionMode = AssertionMode.SOFT;
    }

    @BeforeEach
     void setUp(){
        setLocalDriver();
    }

    @AfterEach
    void tearDown(){
        Selenide.closeWebDriver();
    }
}
