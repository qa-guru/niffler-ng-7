package guru.qa.niffler.dataBase.entity;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendingJson;
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

    public static SpendEntity fromJson(SpendingJson spendingJson) {
        SpendEntity ce = new SpendEntity();
        ce.setId(spendingJson.id());
        ce.setUsername(spendingJson.username());
        ce.setCurrency(spendingJson.currency());
        ce.setSpendDate(new java.sql.Date(spendingJson.spendDate().getTime()));
        ce.setAmount(spendingJson.amount());
        ce.setDescription(spendingJson.description());
        ce.setCategory(CategoryEntity.fromJson(
                spendingJson.category()
        ));
        return ce;
    }
}
