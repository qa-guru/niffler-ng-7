package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface SpendClient {

    @Nonnull
    SpendJson createSpend(SpendJson spend);

    @Nonnull
    CategoryJson createCategory(CategoryJson category);

    @Nonnull
    List<SpendJson> allSpends(String username,
                              @Nullable CurrencyValues currency,
                              @Nullable String from,
                              @Nullable String to);

    @Nonnull
    List<CategoryJson> allCategory(String username);
}
