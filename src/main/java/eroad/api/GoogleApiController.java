package eroad.api;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

/**
 * @author Alon Kodner
 */
@Component
public class GoogleApiController extends ApiController<JSONObject> {
    private static final String TIME_ZONE_API_URL = "https://maps.googleapis.com/maps/api/timezone/json??callback=initMap";
    private static String LOCATION_FIELD = "location";
    private static String TIMESTAMP_FIELD = "timestamp";
    private static String KEY_FIELD = "key";
    private static String STATUS_FIELD = "status";
    private static String TIME_ZONE_ID_FIELD = "timeZoneId";
    // TODO set the Google ApiKey
    private static String API_KEY = "AIzaSyBFqDyKTexVbO6rgP427LZJQ6eHIaYHZXw";

    @Override
    public String getTimeZoneApiUrl(String lat, String lng, Long timeStamp) {
        StringBuilder sb = new StringBuilder();
        sb.append(TIME_ZONE_API_URL);
        String locationString = buildLocationString(lat, lng);
        sb.append("&").append(getParameterString(LOCATION_FIELD, locationString));
        sb.append("&").append(getParameterString(TIMESTAMP_FIELD, String.valueOf(timeStamp)));
        sb.append("&").append(getParameterString(KEY_FIELD, API_KEY));
        return sb.toString();
    }

    @Override
    public boolean isValidResponse(JSONObject responseObject) {
        return responseObject.getString(STATUS_FIELD).equals("OK");
    }

    @Override
    public String getTimeZoneId(JSONObject responseObject) {
        return responseObject.getString(TIME_ZONE_ID_FIELD);
    }

    /**
     * Used for creating string parameters for the REST calls
     *
     * @return String structures as : paramName=paramValue
     */
    protected String getParameterString(String paramName, String paramValue) {
        return (paramName + "=" + paramValue).trim();
    }

    /**
     * @return A comma-separated lat,lng tuple (eg. location=-33.86,151.20)
     */
    private static String buildLocationString(String lat, String lng) {
        return (lat.trim() + "," + lng.trim()).trim();
    }
}