package student.jupiter.extension;

import student.jupiter.extension.browser.SuiteExtension;

public class DatabasesExtension implements SuiteExtension {

    @Override
    public void afterSuite() {
        student.data.Databases.closeConnections();
    }
}
