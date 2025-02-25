package guru.qa.niffler.jupiter.extension;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * Глобальный контекст
 */
public class TestMethodContextExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Holder.INSTANCE.set(context); // добавление контекста для потока
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Holder.INSTANCE.remove();

    }

    /**
     * Создание хранилища для контекста с данными, хранящимся в каждом потоке
     * */
    private enum Holder {
        INSTANCE;

        private final ThreadLocal<ExtensionContext> store = new ThreadLocal<>();

        public void set(ExtensionContext context) {
            store.set(context);
        }

        public ExtensionContext get() {
            return store.get();
        }

        public void remove() {
            store.remove();
        }
    }
    public static ExtensionContext context() {
        return Holder.INSTANCE.get();
    }


}
