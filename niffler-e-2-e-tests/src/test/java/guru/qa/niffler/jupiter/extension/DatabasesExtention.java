package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.tpl.Connections;

public class DatabasesExtention implements SuiteExtension {


    @Override
    public void afterSuite() {
        Connections.closeAllConnections();
    }
}