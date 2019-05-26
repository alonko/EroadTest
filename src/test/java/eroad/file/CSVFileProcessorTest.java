package eroad.file;

import com.opencsv.CSVReader;
import eroad.model.DataModel;
import eroad.model.ModelFieldIndexes;
import eroad.utils.DateUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CSVFileProcessorTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    public final static String INPUT_FILE = "src/test/resources/csv/input.csv";
    public final static String OUTPUT_FILE = "src/test/resources/csv/output.csv";

    private CSVFileProcessor fileProcessor;
    private List<DataModel> models;

    @Before
    public void setup() {
        fileProcessor = new CSVFileProcessor();

        models = new ArrayList<>();
        DataModel model1 = new DataModel();
        model1.setUtcDate(DateUtils.getUTCDate("2013-07-10 02:52:49", DateUtils.INPUT_DATE_FORMATTER));
        model1.setLatitude("-44.490947");
        model1.setLongitude("171.220966");
        model1.setTimeZoneId(ZoneId.of("Pacific/Auckland"));
        model1.setDateByZone(DateUtils.getZonedDateFromUTCDate(model1.getUtcDate(), model1.getTimeZoneId()));
        models.add(model1);

        DataModel model2 = new DataModel();
        model2.setUtcDate(DateUtils.getUTCDate("2013-07-10 02:52:49", DateUtils.INPUT_DATE_FORMATTER));
        model2.setLatitude("-33.912167");
        model2.setLongitude("151.215820");
        model2.setTimeZoneId(ZoneId.of("Australia/Sydney"));
        model2.setDateByZone(DateUtils.getZonedDateFromUTCDate(model2.getUtcDate(), model2.getTimeZoneId()));
        models.add(model2);
    }

    @Test
    public void testGetModelsFromFile() throws IOException {
        List<DataModel> modelsFromFile = fileProcessor.getModelsFromFile(INPUT_FILE);
        assertEquals(3, modelsFromFile.size());
        assertEquals("2013-07-10 02:52:49", modelsFromFile.get(0).getUtcDate().format(DateUtils.INPUT_DATE_FORMATTER));
        assertEquals("-44.490947", modelsFromFile.get(0).getLatitude());
        assertEquals("171.220966", modelsFromFile.get(0).getLongitude());
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileNotFound() throws IOException {
        fileProcessor.getModelsFromFile("TEST.csv");
    }

    @Test
    public void testWriteModelsToFile() throws IOException {
        fileProcessor.writeModelsToFile(OUTPUT_FILE, models);

        CSVReader reader;
        reader = new CSVReader(new FileReader(OUTPUT_FILE));
        String[] nextLine;
        int i = 0;
        while ((nextLine = reader.readNext()) != null) {
            validateModelFromFile(nextLine, models.get(i++));
        }
        assertEquals(models.size(), reader.getLinesRead());
    }

    private void validateModelFromFile(String[] fileLine, DataModel expectedModel) {
        assertEquals(5, fileLine.length);
        assertEquals(expectedModel.getUtcDate().format(DateUtils.INPUT_DATE_FORMATTER), fileLine[ModelFieldIndexes.UTC_DATE.getIndex()]);
        assertEquals(expectedModel.getLatitude(), fileLine[ModelFieldIndexes.LATITUDE.getIndex()]);
        assertEquals(expectedModel.getLongitude(), fileLine[ModelFieldIndexes.LONGITUDE.getIndex()]);
        assertEquals(expectedModel.getTimeZoneId(), ZoneId.of(fileLine[ModelFieldIndexes.TIME_ZONE.getIndex()]));
        assertEquals(expectedModel.getDateByZone().format(DateUtils.OUTPUT_DATE_FORMATTER), fileLine[ModelFieldIndexes.LOCAL_DATE.getIndex()]);
    }

    @Test
    public void testCreateModelsStringRepresentation() {
        List<String[]> list = fileProcessor.createModelsStringRepresentation(models);
        assertEquals(models.size(), list.size());
        assertEquals(models.get(0).getUtcDate().format(DateUtils.INPUT_DATE_FORMATTER), list.get(0)[ModelFieldIndexes.UTC_DATE.getIndex()]);
        assertEquals(models.get(0).getLatitude(), list.get(0)[ModelFieldIndexes.LATITUDE.getIndex()]);
        assertEquals(models.get(0).getLongitude(), list.get(0)[ModelFieldIndexes.LONGITUDE.getIndex()]);
        assertEquals(models.get(0).getTimeZoneId(), ZoneId.of(list.get(0)[ModelFieldIndexes.TIME_ZONE.getIndex()]));
        assertEquals(models.get(0).getDateByZone().format(DateUtils.OUTPUT_DATE_FORMATTER), list.get(0)[ModelFieldIndexes.LOCAL_DATE.getIndex()]);
    }

    @Test
    public void testCreateModelsWithNullModels() {
        exceptionRule.expect(AssertionError.class);
        exceptionRule.expectMessage("Data models can not be null");
        fileProcessor.createModelsStringRepresentation(null);
    }

    @Test
    public void testCreateModel() {
        String utcDate = "2013-07-10 02:52:49";
        String latitude = "-44.490947";
        String longitude = "171.220966";
        String[] fileLine = {utcDate, latitude, longitude};
        DataModel model = fileProcessor.createModel(fileLine);
        assertEquals(utcDate, model.getUtcDate().format(DateUtils.INPUT_DATE_FORMATTER));
        assertEquals(latitude, model.getLatitude());
        assertEquals(longitude, model.getLongitude());
    }
}