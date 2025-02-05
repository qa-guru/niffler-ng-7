package guru.qa.niffler.service.impl;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SpendApiClient implements SpendClient {
  @NotNull
  @Override
  public SpendJson createSpend(SpendJson spend) {
    return null;
  }

  @NotNull
  @Override
  public CategoryJson createCategory(CategoryJson category) {
    return null;
  }

  @Override
  public void removeCategory(CategoryJson category) {
    throw new UnsupportedOperationException();
  }
}
