package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);

    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(anno -> {
                    Category[] categories = anno.categories();
                    Spending[] spendings = anno.spendings();
                    if (spendings.length > 0) {
                        SpendJson spendJson = new SpendJson(
                                null,
                                new Date(),
                                new CategoryJson(
                                        null,
                                        spendings[0].category(),
                                        anno.username(),
                                        false
                                ),
                                spendings[0].currency(),
                                spendings[0].amount(),
                                spendings[0].description(),
                                anno.username()
                        );

                        SpendJson createdSpend = spendApiClient.createSpend(spendJson);

                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                createdSpend
                        );
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(SpendingExtension.NAMESPACE).get(
                extensionContext.getUniqueId(),
                SpendJson.class
        );
    }

}
