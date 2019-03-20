package eroad.api;

/**
 * @author Alon Kodner
 */
public abstract class ApiController<V> {
    /**
     * @param lat Latitude to send to the Api
     * @param lng Longitude to send to the Api
     * @param timeStamp Timestamp to send to the Api
     * @return Rest URL to use for invoking the Api
     */
    public abstract String getTimeZoneApiUrl(String lat, String lng, Long timeStamp);

    /**
     * Used for creating string parameters for the REST calls
     *
     * @return String structures as : paramName=paramValue
     */
    protected String getParameterString(String paramName, String paramValue) {
        return (paramName + "=" + paramValue).trim();
    }

    /**
     * @param response The response to process
     * @return True if the call was successful
     */
    public abstract boolean isValidResponse(V response);

    /**
     * @param response The response to process
     * @return The TimeZoneId returned from the Api
     */
    public abstract String getTimeZoneId(V response);
}