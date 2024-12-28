package guru.qa.niffler.config;

public class LocalConfig implements Config {
    static final LocalConfig INSTANCE = new LocalConfig();

    @Override
    public String frontUrl() {
        return "http://127.0.0.1:3000/";
    }
}