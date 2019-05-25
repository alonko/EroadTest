package eroad.file;

import eroad.model.DataModel;

import java.io.IOException;
import java.util.List;

/**
 * @author Alon Kodner
 */
public interface FileProcessor {
    List<DataModel> getModelsFromFile(String sourceFileName) throws IOException;

    void writeModelsToFile(String targetFileName, List<DataModel> models);
}