package eroad.api;

import com.google.maps.GeoApiContext;
import org.springframework.stereotype.Component;

/**
 * @author Alon Kodner
 *
 * Used for comunication with Google maps GEO API
 */
@Component
public class GoogleAPIController {
    private static final String API_KEY = "";
    private static GeoApiContext context;

    public GoogleAPIController() {
        context = new GeoApiContext.Builder().apiKey(getApiKey()).build();
    }

    public GeoApiContext getGeoApiContext() {
        return context;
    }

    public String getApiKey(){
        return API_KEY;
    }
}