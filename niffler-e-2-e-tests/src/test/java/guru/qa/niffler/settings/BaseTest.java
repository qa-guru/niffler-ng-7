package guru.qa.niffler.settings;

import com.codeborne.selenide.Selenide;

import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Configuration.*;

public class BaseTest {

    private static void setLocalDriver() {
        browser = "chrome";
        browserSize = "1920x1080";
        headless = false;
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
