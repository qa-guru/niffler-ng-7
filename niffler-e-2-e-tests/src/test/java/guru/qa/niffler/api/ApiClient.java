package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public enum ApiClient {
    SPEND_API(Config.getInstance().spendUrl()),
    GH_API(Config.getInstance().spendUrl()),
    FRONT_API(Config.getInstance().spendUrl());

    private Retrofit retrofit;

    ApiClient(String url) {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public Retrofit getINSTANCE() {
        return retrofit;
    }
}
