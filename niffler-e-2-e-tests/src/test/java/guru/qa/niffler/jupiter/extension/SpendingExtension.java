package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.SpendDbClient;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);

  private final SpendClient spendClient = new SpendDbClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
        .ifPresent(userAnno -> {
          if (ArrayUtils.isNotEmpty(userAnno.spendings())) {
            UserJson user = context.getStore(UserExtension.NAMESPACE).get(
                context.getUniqueId(),
                UserJson.class
            );

            final String username = user != null
                ? user.username()
                : userAnno.username();

            final List<SpendJson> createdSpends = new ArrayList<>();

            for (Spending spendAnno : userAnno.spendings()) {
              SpendJson spend = new SpendJson(
                  null,
                  new Date(),
                  new CategoryJson(
                      null,
                      spendAnno.category(),
                      username,
                      false
                  ),
                  spendAnno.currency(),
                  spendAnno.amount(),
                  spendAnno.description(),
                  username
              );

              createdSpends.add(
                  spendClient.createSpend(spend)
              );
            }
            if (user != null) {
              user.testData().spendings().addAll(
                  createdSpends
              );
            } else {
              context.getStore(NAMESPACE).put(
                  context.getUniqueId(),
                  createdSpends
              );
            }
          }
        });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(SpendJson[].class);
  }

  @Override
  @SuppressWarnings("unchecked")
  public SpendJson[] resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return (SpendJson[]) extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), List.class)
        .stream()
        .toArray(SpendJson[]::new);
  }
}
