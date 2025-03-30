package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.SpendRepositorySpringJdbc;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class SpendDbClient implements SpendClient {

    private static final Config CFG = Config.getInstance();

    private final SpendRepository spendRepository = new SpendRepositorySpringJdbc();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );


    @Step("Создаем новый 'spend'")
    @Override
    @Nonnull
    public SpendJson createSpend(SpendJson spend) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() -> SpendJson.fromEntity(
                        spendRepository.create(SpendEntity.fromJson(spend))
                )
        ));
    }

    @Override
    @Nonnull
    public CategoryJson createCategory(CategoryJson category) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() -> CategoryJson.fromEntity(
                        spendRepository.createCategory(CategoryEntity.fromJson(category))
                )
        ));
    }

    @NotNull
    @Override
    public List<SpendJson> allSpends(String username, @Nullable CurrencyValues currency, @Nullable String from, @Nullable String to) {
        return List.of();
    }

    @NotNull
    @Override
    public List<CategoryJson> allCategory(String username) {
        return List.of();
    }
}
