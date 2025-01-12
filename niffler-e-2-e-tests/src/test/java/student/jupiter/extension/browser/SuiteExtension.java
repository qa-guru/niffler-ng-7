package student.jupiter.extension.browser;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public interface SuiteExtension extends BeforeAllCallback {

    @Override
    default void beforeAll(ExtensionContext context) throws Exception {
        ExtensionContext rootContext = context.getRoot();
        rootContext
                .getStore(ExtensionContext.Namespace.GLOBAL)
                .getOrComputeIfAbsent(
                        this.getClass(),
                        key -> {
                            try {
                                beforeSuite(rootContext);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            return new ExtensionContext.Store.CloseableResource() {
                                @Override
                                public void close() throws Throwable {
                                    afterSuite();
                                }
                            };
                        }
                );
    }

    default void beforeSuite(ExtensionContext context) throws Exception {

    }

    default void afterSuite() {

    }
}
