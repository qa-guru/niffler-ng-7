package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

public interface SpendsClient {
    SpendJson createSpend(SpendJson spend);

    CategoryJson createCategory(CategoryJson category);
}
