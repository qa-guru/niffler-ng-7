package student.config;

public interface Config {

    static Config getInstance() {
        return LocalConfig.INSTANCE;
    }

    String frontUrl();

    String authUrl();

    String spendUrl();

    String gatewayUrl();

    String userdataUrl();

    String authJdbcUrl();

    String spendJdbcUrl();

    String userdataJdbcUrl();

    String currencyJdbcUrl();
}
