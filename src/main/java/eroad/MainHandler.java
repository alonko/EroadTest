package eroad;

import eroad.api.ApiController;
import eroad.api.HttpController;
import eroad.file.FileProcessor;
import eroad.file.FileExtension;
import eroad.model.DataModel;
import eroad.utils.DateUtils;
import eroad.utils.FileUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author Alon Kodner
 */
@Component
public class MainHandler {
    private final static Logger LOGGER = Logger.getLogger(MainHandler.class.getName());

    private final HttpController httpController;
    private final FileProcessor fileProcessor;
    private final ApiController<JSONObject> apiController;

    @Autowired
    public MainHandler(HttpController httpController, FileProcessor fileProcessor, ApiController<JSONObject> apiController) {
        this.httpController = httpController;
        this.fileProcessor = fileProcessor;
        this.apiController = apiController;
    }

    /**
     * Read the input file, process the files and write to the output file
     */
    public void processFiles(String inputFileName, String outputFileName) {
        FileUtils.validateFileInput(inputFileName, "Input");
        FileUtils.validateFileInput(outputFileName, "Output");

        if (FileExtension.CSV.toString().equalsIgnoreCase(FileUtils.getFileExtension(inputFileName).toUpperCase())) {
            List<DataModel> models = fileProcessor.getModelsFromFile(inputFileName);
            processModels(models);
            LOGGER.info(models.size() + " lines were processed");
            fileProcessor.writeModelsToFile(outputFileName, models);
        }
    }

    /**
     * Get the objects from the file, call the Api to get the TimeZoneId by the Latitude and Longitude values,
     * calculate the local time and update the objects with the new information.
     *
     * @param models The representation of the objects in the input file
     */
    private void processModels(List<DataModel> models) {
        for (DataModel model : models) {
            Long timestamp = DateUtils.getUTCSeconds(model.getUtcDate());

            if (timestamp != null) {
                String timeZoneUrl = apiController.getTimeZoneApiUrl(model.getLatitude(), model.getLongitude(), timestamp);
                LOGGER.info("timeZoneUrl: " + timeZoneUrl);
                String apiResponse = httpController.sendGetRequest(timeZoneUrl);
                LOGGER.info("apiResponse: " + apiResponse);

                JSONObject responseObject = new JSONObject(apiResponse);
                if (!apiController.isValidResponse(responseObject)) {
                    LOGGER.severe("Api call failed");
                    return;
                }

                String timeZoneId = apiController.getTimeZoneId(responseObject);
                if (StringUtils.isEmpty(timeZoneId)) {
                    LOGGER.severe("Received empty time zone ID");
                    return;
                }

                model.setTimeZoneId(timeZoneId);
                model.setLocalDate(DateUtils.getLocalDate(timestamp, timeZoneId));
            } else {
                LOGGER.severe("Failed to get seconds from date");
            }
        }
    }
}