package eroad;

import com.google.maps.PendingResult;
import com.google.maps.TimeZoneApi;
import com.google.maps.model.LatLng;
import eroad.api.GoogleAPIController;
import eroad.file.FileExtension;
import eroad.file.FileProcessor;
import eroad.model.DataModel;
import eroad.utils.DateUtils;
import eroad.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.ZoneId;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Alon Kodner
 */
@Component
class MainHandler {
    private final static Logger LOGGER = Logger.getLogger(MainHandler.class.getName());

    private final FileProcessor fileProcessor;
    private final GoogleAPIController apiController;

    @Autowired
    MainHandler(FileProcessor fileProcessor, GoogleAPIController apiController) {
        this.fileProcessor = fileProcessor;
        this.apiController = apiController;
    }

    /**
     * Read the input file, process the files and write to the output file
     */
    void processFiles(String inputFileName, String outputFileName) throws IOException {
        FileUtils.validateFileInput(inputFileName, "Input");
        FileUtils.validateFileInput(outputFileName, "Output");
        String fileExtension = FileUtils.getFileExtension(inputFileName);

        if (FileExtension.CSV.toString().equalsIgnoreCase(fileExtension.toUpperCase())) {
            List<DataModel> models = fileProcessor.getModelsFromFile(inputFileName);

            LOGGER.info(models.size() + " lines to process");

            models = models.stream()
                    .map(model -> CompletableFuture.supplyAsync(() -> this.getTimeZone(model)).orTimeout(1, TimeUnit.SECONDS)
                            .thenCompose(completableTimeZoneId -> updateModel(model, completableTimeZoneId)).exceptionally(throwable -> {
                                LOGGER.severe("Failed to process the time zone: " + throwable.getMessage());
                                return null;
                            }))
                    .collect(Collectors.toList())
                    .stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());


            LOGGER.info(models.size() + " lines were processed");
            fileProcessor.writeModelsToFile(outputFileName, models);
        } else {
            LOGGER.severe("Not supported file extension: " + fileExtension);
        }
    }

    CompletableFuture<ZoneId> getTimeZone(DataModel dataModel) {
        LatLng location = new LatLng(Double.parseDouble(dataModel.getLatitude()), Double.parseDouble(dataModel.getLongitude()));
        final CompletableFuture<ZoneId> future = new CompletableFuture<>();
        TimeZoneApi.getTimeZone(apiController.getGeoApiContext(), location).setCallback(new PendingResult.Callback<>() {
            @Override
            public void onResult(TimeZone timeZone) {
                if (timeZone != null) {
                    ZoneId zoneId = ZoneId.of(timeZone.getID());
                    future.complete(zoneId);
                } else {
                    LOGGER.severe("Empty time zone received from Google Api");
                    future.complete(null);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                LOGGER.severe("Failed to get the time zone from Google Api: " + throwable.getMessage());
                future.completeExceptionally(throwable);
            }
        });
        return future;
    }

    CompletableFuture<DataModel> updateModel(DataModel dataModel, CompletableFuture<ZoneId> timeZoneIdCompletableFuture) {
        try {
            ZoneId timeZoneId = timeZoneIdCompletableFuture.get();
            dataModel.setTimeZoneId(timeZoneId);
            dataModel.setDateByZone(DateUtils.getZonedDateFromUTCDate(dataModel.getUtcDate(), timeZoneId));
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.severe("Failed to retrieve time zone information: " + e.getMessage());
        }
        return CompletableFuture.completedFuture(dataModel);
    }
}