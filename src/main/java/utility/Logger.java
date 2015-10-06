package utility;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Logger class for logging events.
 *
 * Logs to file or stdout, or every possible @link{Stream}.
 */
public class Logger {
    private static boolean enabled = false;

    private static OutputStream logFile;
    private static String timestamp = "[hh:mm:ss] ";

    public static final PrintStream OUT = System.out;
    public static final PrintStream ERR = System.err;

    private static final boolean INFO = false;
    private static final boolean ERROR = true;

    /**
     * Class should be used in a static manner.
     */
    protected Logger() {  }

    /**
     * Generates a timestamp in the desired format.
     * @return a formatted timestamp
     */
    private static String timestamp() {
        SimpleDateFormat timestamp = new SimpleDateFormat(Logger.timestamp, Locale.getDefault());
        return timestamp.format(new Date());
    }

    /**
     * Sets the path of the log @link{File}.
     * @param fileName the path to the file
     * @throws FileNotFoundException if write access can not be obtained
     */
    public static void setLogFile(String fileName) throws FileNotFoundException {
        logFile = new FileOutputStream(fileName);
    }

    /**
     * Sets the format of the timestamp.
     * @param format the format String
     */
    public static void setTimestamp(String format) {
        timestamp = format;
    }

    /**
     * Gets the log @link{File}.
     * @return the log file
     */
    public static OutputStream getLogFile() {
        return logFile;
    }

    /**
     * Log a message to stdout.
     * @param msg the message to log
     */
    public static void log(String msg) {
        log(OUT, msg);
    }

    /**
     * Log a message.
     *
     * If it is an info message, it will be written to stdout. If it
     * is an error message, it will appear on stderr.
     * @param msg the message to log
     * @param mode INFO for info messages, ERROR for error messages
     */
    public static void log(String msg, boolean mode) {
        if (mode == ERROR && enabled) {
            log(ERR, msg);
        } else if (mode == INFO && enabled) {
            log(OUT, msg);
        }
    }

    /**
     * Log a message to the @link{File}.
     * @param msg the message to log
     */
    public static void logToFile(String msg) {
        log(logFile, msg);
    }

    /**
     * Log a message to a @link{PrintStream}.
     * @param ps the @link{PrintStream} to write to
     * @param msg the message to log
     */
    public static void log(PrintStream ps, String msg) {
        log(new BufferedOutputStream(ps), msg);
    }

    /**
     * Log a message to an @link{OutputStream}.
     * @param stream the @link{OutputStream} to log to
     * @param msg the message to log
     */
    public static void log(OutputStream stream, String msg) {
        if (enabled) {
            try {
                stream.write(timestamp().getBytes(Charset.defaultCharset()));

                stream.write(msg.getBytes(Charset.defaultCharset()));
                stream.write('\n');

                stream.flush();
            } catch (IOException e) {
                System.out.println(msg);
            }
        }
    }

    /**
     * A function to force enabling the logger.
     */
    public static void forceEnable() {
        enabled = true;
    }
}
