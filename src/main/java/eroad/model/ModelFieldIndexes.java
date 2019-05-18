package eroad.model;

/**
 * @author Alon Kodner
 * <p>
 * Used for indicating the index of each model field in the CSV
 */
public enum ModelFieldIndexes {
    UTC_DATE(0),
    LATITUDE(1),
    LONGITUDE(2),
    TIME_ZONE(3),
    LOCAL_DATE(4);

    private int index;

    ModelFieldIndexes(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}