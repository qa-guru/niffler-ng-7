package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.CategoryDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

public class CategoryExtension implements
        BeforeEachCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final CategoryDbClient categoryDbClient = new CategoryDbClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(anno -> {
                    Category[] categories = anno.categories();
                    CategoryJson created;

                    if (categories.length > 0) {

                        String categoryName = RandomDataUtils.randomCategoryName();
                        //Проверяем, что у юзера категории с таким именем еще нет
                        Optional<CategoryJson> foundCategory = categoryDbClient
                                .findCategoryByUsernameAndCategoryName(anno.username(), categoryName);
                        //Если категория с таким именем существует, то ее будем далее использовать
                        if (foundCategory.isPresent()) {
                            created = foundCategory.get();
                        } else { //Если категории с таким именем еще нет, то создаем ее
                            CategoryJson categoryJson = new CategoryJson(
                                    null,
                                    categoryName,
                                    anno.username(),
                                    categories[0].archived()
                            );
                            created = categoryDbClient.createCategory(categoryJson);
                        }
                        //Архивируем категорию, если этого требует предусловие
                        if (categories[0].archived()) {
                            CategoryJson categoryToArchive = new CategoryJson(
                                    created.id(),
                                    created.name(),
                                    created.username(),
                                    true
                            );
                            created = categoryDbClient.updateCategory(categoryToArchive);
                        }

                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                created
                        );
                    }

                });
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (category != null) {
            if (!category.archived()) {
                category = new CategoryJson(
                        category.id(),
                        category.name(),
                        category.username(),
                        true
                );
                categoryDbClient.updateCategory(category);
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }
}
