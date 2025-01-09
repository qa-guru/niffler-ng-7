package guru.qa.niffler.config;

public interface Config {
    String PROJECT_NAME = "niffler";

    static Config getInstance() {
        return LocalConfig.INSTANCE;
    }
    String frontUrl();
    String authUrl();
    String authJdbcUrl();
    String gatewayUrl();
    String userDataUrl();
    String userDataJdbcUrl();
    String spendUrl();
    String spendJdbcUrl();
    String currencyJdbcUrl();
    String ghUrl();
}