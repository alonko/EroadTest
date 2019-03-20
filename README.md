##Alon Kodner - Eroad Test

This application will read a CSV file which contains UTC datetime, latitude and longitude columns and by using Google API will crate an output file which will append the timezone the vehicle is in and the localised datetime.

###Running the application
`mvn package && java -jar target/AlonTest-1.0-SNAPSHOT.jar`

Add as parameters the source file path followed by the destination file path or alternatively don't provide any arguments and the application will use the default files: 
* src/main/resources/csv/input.csv
* src/main/resources/csv/output.csv

Please set a Google Api Key in `eroad.api.GoogleApiController.API_KEY` field.