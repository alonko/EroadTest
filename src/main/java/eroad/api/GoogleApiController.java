package eroad.api;

import com.google.maps.GeoApiContext;
import org.springframework.stereotype.Component;

/**
 * @author Alon Kodner
 *
 * Used for comunication with Google maps GEO API
 */
@Component
public class GoogleApiController {
    private static final String API_KEY = "AIzaSyBFqDyKTexVbO6rgP427LZJQ6eHIaYHZXw";
    private static GeoApiContext context;

    public GoogleApiController() {
        context = new GeoApiContext.Builder().apiKey(API_KEY).build();
    }

    public GeoApiContext getGeoApiContext() {
        return context;
    }
}