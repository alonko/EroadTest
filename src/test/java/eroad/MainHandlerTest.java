package eroad;

import eroad.api.GoogleAPIController;
import eroad.file.CSVFileProcessorTest;
import eroad.file.FileProcessor;
import eroad.model.DataModel;
import eroad.utils.DateUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.time.ZoneId;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MainHandlerTest {
    @Mock
    private FileProcessor fileProcessor;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private MainHandler getMainHandler(FileProcessor fileProcessor, GoogleAPIController googleApiController) {
        return new MainHandler(fileProcessor, googleApiController);
    }

    @Test
    public void testGetTimeZone() {
        MainHandler mainHandler = getMainHandler(fileProcessor, new GoogleAPIController());
        DataModel model = new DataModel();
        model.setUtcDate(DateUtils.getUTCDate("2013-07-10 02:52:49", DateUtils.INPUT_DATE_FORMATTER));
        model.setLatitude("-44.490947");
        model.setLongitude("171.220966");
        CompletableFuture<ZoneId> timeZoneCompletableFuture = mainHandler.getTimeZone(model);
        try {
            ZoneId timeZoneID = timeZoneCompletableFuture.get();
            assertEquals(ZoneId.of("Pacific/Auckland"), timeZoneID);
        } catch (InterruptedException | ExecutionException e) {
            fail();
        }
    }

    @Test
    public void testGetTimeZone_WrongApiKey() {
        GoogleAPIController googleApiController = new GoogleAPIController() {
            @Override
            public String getApiKey() {
                return "test";
            }
        };

        MainHandler mainHandler = getMainHandler(fileProcessor, googleApiController);
        DataModel model = new DataModel();
        model.setUtcDate(DateUtils.getUTCDate("2013-07-10 02:52:49", DateUtils.INPUT_DATE_FORMATTER));
        model.setLatitude("-44.490947");
        model.setLongitude("171.220966");

        exceptionRule.expect(IllegalStateException.class);
        exceptionRule.expectMessage("Invalid API key.");

        mainHandler.getTimeZone(model);
    }

    @Test
    public void testUpdateModel() {
        MainHandler mainHandler = getMainHandler(fileProcessor, new GoogleAPIController());
        DataModel model = new DataModel();
        model.setUtcDate(DateUtils.getUTCDate("2013-07-10 02:52:49", DateUtils.INPUT_DATE_FORMATTER));
        model.setLatitude("-44.490947");
        model.setLongitude("171.220966");

        CompletableFuture<ZoneId> timeZoneCompletableFuture = mock(CompletableFuture.class);
        try {
            when(timeZoneCompletableFuture.get()).thenReturn(ZoneId.of("Pacific/Auckland"));
        } catch (InterruptedException | ExecutionException e) {
            fail();
        }

        mainHandler.updateModel(model, timeZoneCompletableFuture);
        assertEquals(ZoneId.of("Pacific/Auckland"), model.getTimeZoneId());
        assertEquals("2013-07-10T14:52:49", model.getDateByZone().format(DateUtils.OUTPUT_DATE_FORMATTER));
    }

    @Test
    public void testProcessFiles() throws IOException {
        MainHandler mainHandler = getMainHandler(fileProcessor, new GoogleAPIController());
        mainHandler.processFiles(CSVFileProcessorTest.INPUT_FILE, CSVFileProcessorTest.OUTPUT_FILE);
        verify(fileProcessor).getModelsFromFile(CSVFileProcessorTest.INPUT_FILE);
        verify(fileProcessor).writeModelsToFile(eq(CSVFileProcessorTest.OUTPUT_FILE), any());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessFiles_NotSupportedFileExtension() throws IOException {
        MainHandler mainHandler = getMainHandler(fileProcessor, new GoogleAPIController());
        mainHandler.processFiles("test.txt", CSVFileProcessorTest.OUTPUT_FILE);
    }
}