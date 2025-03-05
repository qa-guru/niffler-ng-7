package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.DataBases;

public class DatabaseExtension implements SuiteExtension {
    @Override
    public void afterSuite() {
        DataBases.closeAllConnections();
    }
}
