package guru.qa.niffler.data.entity.spend;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
public class SpendEntity implements Serializable {
    private UUID id;

    private String username;

    private CurrencyValues currency;

    private Date spendDate;

    private Double amount;

    private String description;

    private CategoryEntity category;


    public static SpendEntity fromJson(SpendJson json) {
        final CategoryJson category = json.category();

        SpendEntity spendEntity = new SpendEntity();

        spendEntity.setId(json.id());
        spendEntity.setSpendDate(new java.sql.Date(json.spendDate().getTime()));
        spendEntity.setCategory(CategoryEntity.fromJson(category));
        spendEntity.setCurrency(json.currency());
        spendEntity.setAmount(json.amount());
        spendEntity.setDescription(json.description());
        spendEntity.setUsername(json.username());

        return spendEntity;
    }

}
