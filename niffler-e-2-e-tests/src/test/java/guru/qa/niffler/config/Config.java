package guru.qa.niffler.config;

public interface Config {
    String PROJECT_NAME = "niffler";

    static Config getInstance() {
        return LocalConfig.INSTANCE;
    }
    String frontUrl();
    String authUrl();
    String gatewayUrl();
    String userDataUrl();
    String spendUrl();
    String ghUrl();
}