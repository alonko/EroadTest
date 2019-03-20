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
 */
@Component
public class CSVFileProcessor implements FileProcessor {
    private final static Logger LOGGER = Logger.getLogger(CSVFileProcessor.class.getName());

    /**
     * @param sourceFileName The input file
     * @return List of models represented in the file
     */
    @Override
    public List<DataModel> getModelsFromFile(String sourceFileName) {
        CSVReader reader;
        List<DataModel> inputList = new ArrayList<>();
        try {
            reader = new CSVReader(new FileReader(sourceFileName));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                DataModel model = createModel(nextLine);
                inputList.add(model);
            }
        } catch (IOException e) {
            LOGGER.severe("Failed to process file");
            e.printStackTrace();
        }
        return inputList;
    }

    /**
     * @param targetFileName The output file
     * @param models The updated models to be written to the output file
     */
    @Override
    public void writeModelsToFile(String targetFileName, List<DataModel> models) {
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(targetFileName));

            String[] entries = new String[5];
            for (DataModel model : models) {
                entries[ModelFieldIndexes.UTC_DATE.getIndex()] = model.getUtcDate().trim();
                entries[ModelFieldIndexes.LATITUDE.getIndex()] = model.getLatitude().trim();
                entries[ModelFieldIndexes.LONGITUDE.getIndex()] = model.getLongitude().trim();
                entries[ModelFieldIndexes.TIME_ZONE.getIndex()] = model.getTimeZoneId().trim();
                entries[ModelFieldIndexes.LOCAL_DATE.getIndex()] = model.getLocalDate().trim();
                writer.writeNext(entries, false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param fileLine Single line from the CSV file
     * @return Object representation of a single CSV line
     */
    private DataModel createModel(String[] fileLine) {
        DataModel model = new DataModel();
        model.setUtcDate(fileLine[ModelFieldIndexes.UTC_DATE.getIndex()]);
        model.setLatitude(fileLine[ModelFieldIndexes.LATITUDE.getIndex()]);
        model.setLongitude(fileLine[ModelFieldIndexes.LONGITUDE.getIndex()]);

        return model;
    }
}