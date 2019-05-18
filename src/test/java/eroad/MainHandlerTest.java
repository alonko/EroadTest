package eroad;

import eroad.api.GoogleApiController;
import eroad.file.FileProcessor;
import eroad.model.DataModel;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainHandlerTest {
    @Mock
    private FileProcessor fileProcessor;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private MainHandler getMainHandler(FileProcessor fileProcessor, GoogleApiController googleApiController) {
        return new MainHandler(fileProcessor, googleApiController);
    }

    @Test
    public void testGetTimeZone() {
        MainHandler mainHandler = getMainHandler(fileProcessor, new GoogleApiController());
        DataModel model = new DataModel();
        model.setUtcDate("2013-07-10 02:52:49");
        model.setLatitude("-44.490947");
        model.setLongitude("171.220966");
        CompletableFuture<String> timeZoneCompletableFuture = mainHandler.getTimeZone(model);
        try {
            String timeZoneID = timeZoneCompletableFuture.get();
            assertEquals("Pacific/Auckland", timeZoneID);
        } catch (InterruptedException | ExecutionException e) {
            fail();
        }
    }

    @Test
    public void testGetTimeZone_WrongApiKey() {
        GoogleApiController googleApiController = new GoogleApiController() {
            @Override
            public String getApiKey() {
                return "test";
            }
        };

        MainHandler mainHandler = getMainHandler(fileProcessor, googleApiController);
        DataModel model = new DataModel();
        model.setUtcDate("2013-07-10 02:52:49");
        model.setLatitude("-44.490947");
        model.setLongitude("171.220966");

        exceptionRule.expect(IllegalStateException.class);
        exceptionRule.expectMessage("Invalid API key.");

        mainHandler.getTimeZone(model);
    }

    @Test
    public void testUpdateModel() {
        MainHandler mainHandler = getMainHandler(fileProcessor, new GoogleApiController());
        DataModel model = new DataModel();
        model.setUtcDate("2013-07-10 02:52:49");
        model.setLatitude("-44.490947");
        model.setLongitude("171.220966");

        CompletableFuture timeZoneCompletableFuture = mock(CompletableFuture.class);
        try {
            when(timeZoneCompletableFuture.get()).thenReturn("Pacific/Auckland");
        } catch (InterruptedException | ExecutionException e) {
            fail();
        }

        mainHandler.updateModel(model, timeZoneCompletableFuture);
        assertEquals("Pacific/Auckland", model.getTimeZoneId());
        assertEquals("2013-07-10T14:52:49", model.getLocalDate());
    }
}