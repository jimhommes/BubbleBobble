package utility;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Test for the Logger class.
 */
public class LoggerTest {

    private File testFile1;
    private File testFile2;
    private File testFile3;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private PrintStream outStream;

    /**
     * This creates a checkstyle warning because it believed that 
     * folder must be private, but @Rule believes that it should be public.
     */
    @Rule
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public TemporaryFolder folder = new TemporaryFolder();

    /**
     * Set up the test class.
     */
    @Before
    public void setUp() {
        try {
            Logger.setEnabled(true);
            testFile1 = folder.newFile("loggingTestFile1.txt");
            testFile2 = folder.newFile("loggingTestFile2.txt");
            testFile3 = folder.newFile("loggingTestFile3.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            outStream = new PrintStream(outContent, true, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            fail();
        }
    }


    /**
     * Test setting the log file path.
     * @throws FileNotFoundException if file path does not exist.
     */
    @Test
    public void testSetLogFile() throws FileNotFoundException {
        Logger.setLogFile(testFile1.getAbsolutePath());
        assertNotNull(Logger.getLogFile());

    }

    /**
     * Test logging to a file.
     * @throws IOException if the test file is not found.
     */
    @Test
    public void testLogToFile() throws IOException {
        Logger.setLogFile(testFile2.getAbsolutePath());
        Logger.logToFile("Test log");
        String text;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(testFile2), "UTF-8"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            text = sb.toString();
        }

        assertThat(text, containsString("Test log"));

    }

    /**
     * Test default log.
     */
    @Test
    public void testLog() {
        Logger.log(outStream, "Test log to console");
        try {
            assertThat(outContent.toString("UTF-8"), containsString("Test log to console"));
        } catch (UnsupportedEncodingException e) {
            fail();
        }
    }


    /**
     * Test setting the format of the timestamp.
     * @throws IOException if the test file is not found.
     */
    @Test
    public void testSetTimestampFormat() throws IOException {
        Logger.setLogFile((testFile3.getAbsolutePath()));
        Logger.setTimestamp("yyyy");
        Logger.logToFile("");
        String text;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(testFile3), "UTF-8"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            
            text = sb.toString();
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        String expected = dateFormat.format(date);
        assertEquals(text.trim(), expected.trim());

    }

}