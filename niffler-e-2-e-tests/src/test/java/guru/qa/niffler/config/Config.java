package guru.qa.niffler.config;

public interface Config {
    static Config getInstance() {
        return LocalConfig.INSTANCE;
    }

    String frontUrl();

    String authUrl();

    String gatewayUrl();

    String userdataUrl();

    String userdataJDBCUrl();

    String spendUrl();

    String spendJDBCUrl();

    String currencyJBDCUrl();

    String ghUrl();
}
