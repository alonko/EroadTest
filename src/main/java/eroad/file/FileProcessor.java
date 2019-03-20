package eroad.file;

import eroad.model.DataModel;

import java.util.List;

/**
 * @author Alon Kodner
 */
public interface FileProcessor {
    List<DataModel> getModelsFromFile(String sourceFileName);

    void writeModelsToFile(String targetFileName, List<DataModel> models);
}