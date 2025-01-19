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
}
