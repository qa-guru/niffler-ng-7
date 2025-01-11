package guru.qa.niffler.dataBase.entity;

import guru.qa.niffler.model.CategoryJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class CategoryEntity implements Serializable {
    private UUID id;
    private String name;
    private String username;
    private Boolean archived;

    public static CategoryEntity fromJson(CategoryJson categoryJson) {
        CategoryEntity ce = new CategoryEntity();
        ce.setId(categoryJson.id());
        ce.setName(categoryJson.name());
        ce.setUsername(categoryJson.username());
        ce.setArchived(categoryJson.archived());
        return ce;
    }
}
