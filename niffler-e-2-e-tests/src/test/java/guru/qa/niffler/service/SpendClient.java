package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public interface SpendClient {
    SpendJson createSpend(SpendJson spend);

    @Nonnull
    CategoryJson createCategory(CategoryJson category);

    @NotNull
    CategoryJson updateCategory(CategoryJson category);

    void removeCategory(CategoryJson category);
}