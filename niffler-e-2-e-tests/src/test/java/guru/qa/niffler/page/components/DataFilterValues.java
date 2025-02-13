package guru.qa.niffler.page.components;

public enum DataFilterValues {

    TODAY("Today"),
    WEEK("last week"),
    MONTH("Last month");

    public final String text;

    DataFilterValues(String text) {
        this.text = text;
    }
}
