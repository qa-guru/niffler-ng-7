package guru.qa.niffler.helpers.jupiter.extension;

import guru.qa.niffler.dataBase.dbConnection.DataBases;

public class DataBasesExtension implements SuiteExtension{

    @Override
    public void afterSuite() {
        DataBases.closeAllConnections();
    }
}
