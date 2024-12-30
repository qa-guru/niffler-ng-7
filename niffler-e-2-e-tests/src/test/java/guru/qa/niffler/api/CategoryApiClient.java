package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryApiClient {

  private final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(Config.getInstance().spendUrl())
      .addConverterFactory(JacksonConverterFactory.create())
      .build();

  private final CategoryApi categoryApi = retrofit.create(CategoryApi.class);


  public CategoryJson createCategory(CategoryJson category) {
    final Response<CategoryJson> response;
    try {
      response = categoryApi.addCategory(category)
              .execute();
    } catch (IOException e) {
        throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }


  public CategoryJson editCategory(CategoryJson category) {
    final Response<CategoryJson> response;
    try {
      response = categoryApi.editCategory(category)
              .execute();
    } catch (IOException e) {
    throw new AssertionError(e);
    }
  assertEquals(200, response.code());
    return response.body();
  }

  public CategoryJson getCategories(String username, Boolean excludeArchived) {
    final Response<CategoryJson> response;
    try {
      response = categoryApi.getCategories(username, excludeArchived)
              .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

}
