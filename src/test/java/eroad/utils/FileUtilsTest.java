package eroad.utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author Alon Kodner
 */
@RunWith(SpringRunner.class)
public class FileUtilsTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private String fileDescription;

    @Before
    public void setup() {
        fileDescription = "desc";
    }

    @Test
    public void validateFileInputTest_valid() {
        FileUtils.validateFileInput("test.csv", fileDescription);
    }

    @Test
    public void validateFileInputTest_emptyFile() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage(fileDescription + " file required");
        FileUtils.validateFileInput(null, fileDescription);
    }

    @Test
    public void validateFileInputTest_NoFileExtension() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage(fileDescription + " file has not valid file name: test");
        FileUtils.validateFileInput("test", fileDescription);
    }

    @Test
    public void validateFileInputTest_NotSupportedFileExtension() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage(fileDescription + " file is of not supported file type: txt, supported file types: CSV");
        FileUtils.validateFileInput("test.txt", fileDescription);
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