package model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

import org.junit.Test;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Matthijs on 15-09-15.
 */
public class LoggerTest {

    private File testFile1;
    private File testFile2;
    private File testFile3;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private PrintStream outStream;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUp() {
        try {
            testFile1 = folder.newFile("loggingTestFile1.txt");
            testFile2 = folder.newFile("loggingTestFile2.txt");
            testFile3 = folder.newFile("loggingTestFile3.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        outStream = new PrintStream(outContent);

    }


    @Test
    public void testSetLogFile() throws FileNotFoundException {
        Logger.setLogFile(testFile1.getAbsolutePath());
        assertNotNull(Logger.getLogFile());

    }

    @Test
    public void testLogToFile() throws IOException {
        Logger.setLogFile((testFile2.getAbsolutePath()));
        Logger.logToFile("Test log");
        String text;
        BufferedReader br = new BufferedReader(new FileReader(testFile2));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            text = sb.toString();
        } finally {
            br.close();
        }

        assertThat(text, containsString("Test log"));

    }

    @Test
    public void testLog() {
        Logger.log(outStream, "Test log to console");
        assertThat(outContent.toString(), containsString("Test log to console"));
    }


    @Test
    public void testSetTimestampFormat() throws IOException {
        Logger.setLogFile((testFile3.getAbsolutePath()));
        Logger.setTimestampFormat("yyyy");
        Logger.logToFile("");
        String text;
        BufferedReader br = new BufferedReader(new FileReader(testFile3));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            text = sb.toString();
        } finally {
            br.close();
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        String expected = dateFormat.format(date) + "\n";
        assertEquals(text, expected);
        
    }

}
