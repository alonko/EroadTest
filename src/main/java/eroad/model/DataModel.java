package eroad.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Alon Kodner
 * <p>
 * Object representation of the CSV model
 */
public class DataModel {
    private ZonedDateTime utcDate;
    private String latitude;
    private String longitude;
    private ZoneId timeZoneId;
    private ZonedDateTime dateByZone;

    public ZonedDateTime getUtcDate() {
        return utcDate;
    }

    public void setUtcDate(ZonedDateTime utcDate) {
        this.utcDate = utcDate;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public ZoneId getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(ZoneId timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public ZonedDateTime getDateByZone() {
        return dateByZone;
    }

    public void setDateByZone(ZonedDateTime dateByZone) {
        this.dateByZone = dateByZone;
    }
}