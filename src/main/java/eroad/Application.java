package eroad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.logging.Logger;

/**
 * @author Alon Kodner
 */
@SpringBootApplication
public class Application {
    private final static Logger LOGGER = Logger.getLogger(Application.class.getName());
    private final static String INPUT_FILE = "src/main/resources/csv/input.csv";
    private final static String OUTPUT_FILE = "src/main/resources/csv/output.csv";

    private static String inputFile;
    private static String outputFile;

    private final MainHandler mainHandler;

    @Autowired
    public Application(MainHandler mainHandler) {
        this.mainHandler = mainHandler;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            inputFile = INPUT_FILE;
            outputFile = OUTPUT_FILE;
        } else if (args.length == 2) {
            inputFile = args[0];
            outputFile = args[1];
        } else {
            LOGGER.severe("Please enter input and output file paths or don't set parameters for default files");
            System.exit(1);
        }
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            mainHandler.processFiles(inputFile, outputFile);
            System.exit(0);
        };
    }
}