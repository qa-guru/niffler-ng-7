package guru.qa.niffler.jupiter;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public interface SuiteExtension extends BeforeAllCallback, AfterAllCallback {

    /*
    1) Быть уверенным, что SuiteExtension будет выполняться перед каждым тестовым классом
    2) Если мы выполним какой то класс перед загрузкой самого первого тестового класса, то это и будет beforeSuite()
    3) При этом, для 2, 3 и т.д. тестовых классов, больше не будем вызывать beforeSuite()
    4) Когда все все тесты завершаться, вызовем afterSuite()
     */
    @Override
    default void beforeAll(ExtensionContext context) throws Exception {
        final ExtensionContext rootContext = context.getRoot();
        rootContext.getStore(ExtensionContext.Namespace.GLOBAL).getOrComputeIfAbsent(
                this.getClass(), key -> { // попадаем только в самый 1 раз
                    beforeSuite(rootContext);
                    return (ExtensionContext.Store.CloseableResource) this::afterSuite;
                }
        );
    }
    default void beforeSuite(ExtensionContext context) {

    }

    default void afterSuite() {}

    @Override
    default void afterAll(ExtensionContext context) throws Exception {

    }
}
