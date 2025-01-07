package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.dataBase.entity.CategoryEntity;
import guru.qa.niffler.dataBase.entity.SpendEntity;

import java.util.Date;
import java.util.UUID;

public record SpendingJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("spendDate")
        Date spendDate,
        @JsonProperty("category")
        CategoryJson category,
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("amount")
        Double amount,
        @JsonProperty("description")
        String description,
        @JsonProperty("username")
        String username) {

    public static SpendingJson fromEntity(SpendEntity entity) {
        final CategoryEntity category = entity.getCategory();
        final String username = entity.getUsername();

        return new SpendingJson(
                entity.getId(),
                entity.getSpendDate(),
                new CategoryJson(
                        category.getId(),
                        category.getName(),
                        username,
                        category.getArchived()
                ),
                entity.getCurrency(),
                entity.getAmount(),
                entity.getDescription(),
                username
        );
    }
}
