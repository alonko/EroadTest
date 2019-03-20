package eroad.utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author Alon Kodner
 */
@RunWith(SpringRunner.class)
public class FileUtilsTest {
    private String fileDescription;

    @Before
    public void setup(){
        fileDescription = "desc";
    }

    @Test
    public void validateFileInputTest_valid() {
        FileUtils.validateFileInput("test.csv", fileDescription);
    }

    @Test
    public void validateFileInputTest_emptyFile() {
        try {
            FileUtils.validateFileInput(null, fileDescription);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(fileDescription + " file required", e.getMessage());
        }
    }

    @Test
    public void validateFileInputTest_NoFileExtension() {
        try {
            FileUtils.validateFileInput("test", fileDescription);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(fileDescription + " file has not valid file name: test", e.getMessage());
        }
    }

    @Test
    public void validateFileInputTest_NotSupportedFileExtension() {
        try {
            FileUtils.validateFileInput("test.txt", fileDescription);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(fileDescription + " file is of not supported file type: txt, supported file types: CSV", e.getMessage());
        }
    }

    @Test
    public void getFileExtensionTest() {
        assertNotNull("csv", FileUtils.getFileExtension("test.csv"));
        assertNotNull("txt", FileUtils.getFileExtension("LongFileName_forTest_123.txt"));
        assertNotNull("", FileUtils.getFileExtension("file_noExtension"));
    }

    @Test
    public void isValidFileExtensionTest() {
        assertTrue(FileUtils.isValidExtension("csv"));
        assertFalse(FileUtils.isValidExtension("txt"));
        assertFalse(FileUtils.isValidExtension("tst"));
    }

}