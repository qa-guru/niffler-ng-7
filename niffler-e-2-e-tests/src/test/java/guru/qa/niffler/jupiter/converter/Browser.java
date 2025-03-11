package guru.qa.niffler.jupiter.converter;

import com.codeborne.selenide.SelenideConfig;

public enum Browser {
    CHROME("chrome"),
    FIREFOX("firefox");

    private final String browser;

    Browser(String browser) {
        this.browser = browser;
    }

    public SelenideConfig getConfig() {
        return new SelenideConfig()
                .browser(browser)
                .pageLoadStrategy("eager")
                .timeout(5000L);
    }
}