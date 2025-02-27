package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.CategoryApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import static guru.qa.niffler.util.RandomDataUtils.*;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace
            .create(CategoryExtension.class);

    private final CategoryApiClient categoryApi = new CategoryApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                        for (Category annoCategory : userAnno.categories()) {
                            CategoryJson categoryJson = new CategoryJson(
                                    null,
                                    randomCategoryName(),
                                    userAnno.username(),
                                    false
                            );
                            CategoryJson createdCategory = categoryApi.addCategory(categoryJson);
                            if (annoCategory.archived()) {
                                CategoryJson archiveCategory = new CategoryJson(
                                        createdCategory.id(),
                                        createdCategory.name(),
                                        userAnno.username(),
                                        true
                                );
                                createdCategory = categoryApi.updateCategory(archiveCategory);
                            }
                            context.getStore(NAMESPACE).put(
                                    context.getUniqueId(),
                                    createdCategory);
                            break; // забирает только 1 значение. Убрать если нужно каждое
                        }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext,
                                         ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(CategoryExtension.NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (category != null && !category.archived()) {
            categoryApi.updateCategory(new CategoryJson(
                    category.id(),
                    category.name(),
                    category.username(),
                    true
            ));
        }
    }
}
