package eroad.file;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import eroad.model.DataModel;
import eroad.model.ModelFieldIndexes;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Alon Kodner
 * <p>
 * Used for working with CSV files
 */
@Component
public class CSVFileProcessor implements FileProcessor {
    private final static Logger LOGGER = Logger.getLogger(CSVFileProcessor.class.getName());

    /**
     * @param sourceFileName The input file
     * @return List of models represented in the file
     */
    @Override
    public List<DataModel> getModelsFromFile(String sourceFileName) throws IOException {
        List<DataModel> inputList = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(sourceFileName))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                DataModel model = createModel(nextLine);
                inputList.add(model);
            }
        } catch (IOException exception) {
            LOGGER.severe("Failed to process file");
            exception.printStackTrace();
            throw exception;
        }
        return inputList;
    }

    /**
     * @param targetFileName The output file
     * @param models         The models to be written to the output file
     */
    @Override
    public void writeModelsToFile(String targetFileName, List<DataModel> models) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(targetFileName))) {
            List<String[]> modelsToWrite = createModelsStringRepresentation(models);
            writer.writeAll(modelsToWrite, false);
        } catch (IOException e) {
            LOGGER.severe("Failed to write files to file");
            e.printStackTrace();
        }
    }

    List<String[]> createModelsStringRepresentation(List<DataModel> models) {
        assert models != null : "Data models can not be null";
        List<String[]> modelsToWrite = new ArrayList<>(models.size());
        for (DataModel model : models) {
            String[] entries = new String[5];
            entries[ModelFieldIndexes.UTC_DATE.getIndex()] = model.getUtcDate().trim();
            entries[ModelFieldIndexes.LATITUDE.getIndex()] = model.getLatitude().trim();
            entries[ModelFieldIndexes.LONGITUDE.getIndex()] = model.getLongitude().trim();
            entries[ModelFieldIndexes.TIME_ZONE.getIndex()] = model.getTimeZoneId().trim();
            entries[ModelFieldIndexes.LOCAL_DATE.getIndex()] = model.getLocalDate().trim();
            modelsToWrite.add(entries);
        }
        return modelsToWrite;
    }

    /**
     * @param fileLine Single line from the CSV file
     * @return Object representation of a single CSV line
     */
    DataModel createModel(String[] fileLine) {
        assert fileLine.length == 3 : "Each line needs to contain 3 values";
        DataModel model = new DataModel();
        try {
            model.setUtcDate(fileLine[ModelFieldIndexes.UTC_DATE.getIndex()]);
            model.setLatitude(fileLine[ModelFieldIndexes.LATITUDE.getIndex()]);
            model.setLongitude(fileLine[ModelFieldIndexes.LONGITUDE.getIndex()]);
        } catch (Exception e) {
            LOGGER.severe("Failed to create the model");
            e.printStackTrace();
        }

        return model;
    }
}