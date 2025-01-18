package guru.qa.niffler.config;

import org.apache.kafka.common.protocol.types.Field;

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

    String authJDBCUrl();
}
