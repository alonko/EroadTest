package eroad.utils;

import eroad.file.FileExtension;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Alon Kodner
 */
public class FileUtils {
    /**
     * @param fileName - The file name to validate
     * @param fileDescription - The description of the file for error handling (Input/Output)
     */
    public static void validateFileInput(String fileName, String fileDescription) {
        if (StringUtils.isEmpty(fileName)) {
            throw new IllegalArgumentException(fileDescription + " file required");
        }

        String fileExtension = getFileExtension(fileName);
        if (StringUtils.isEmpty(fileExtension)) {
            throw new IllegalArgumentException(fileDescription + " file has not valid file name: " + fileName);
        }

        if (!isValidExtension(fileExtension)) {
            String fileExtensions = Stream.of(FileExtension.values()).
                    map(FileExtension::name).
                    collect(Collectors.joining(", "));
            throw new IllegalArgumentException(fileDescription + " file is of not supported file type: " + fileExtension + ", supported file types: " + fileExtensions);
        }
    }

    /**
     *
     * @param fileExtension The file extension (csv)
     * @return true if the file type is supported (currently only csv is supported)
     */
    public static boolean isValidExtension(String fileExtension) {
        for (FileExtension extension : FileExtension.values()) {
            if (extension.toString().equalsIgnoreCase(fileExtension))
                return true;
        }
        return false;
    }

    /**
     *
     * @param fileName the full file name
     * @return the extension of the file (csv)
     */
    public static String getFileExtension(String fileName) {
        assert !StringUtils.isEmpty(fileName) : "file name required";
        String extension = "";
        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            extension = fileName.substring(index + 1);
        }
        return extension;
    }
}