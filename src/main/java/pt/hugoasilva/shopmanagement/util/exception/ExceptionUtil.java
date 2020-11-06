package pt.hugoasilva.shopmanagement.util.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintStream;

/**
 * Exception Util Class
 *
 * @author Hugo Silva
 * @version 2020-111-01
 */

public class ExceptionUtil {

    private final static Logger LOGGER = LogManager.getLogger(ExceptionUtil.class.getName());

    /**
     * Initialize exception handler
     */
    public static void init() {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        System.setOut(createLoggingProxy(System.out));
        System.setErr(createLoggingProxy(System.err));
    }

    /**
     * Initialize logger
     *
     * @param realPrintStream logger's print stream
     * @return print stream
     */
    public static PrintStream createLoggingProxy(final PrintStream realPrintStream) {
        return new PrintStream(realPrintStream) {
            @Override
            public void print(final String string) {
                LOGGER.info(string);
            }

            @Override
            public void println(final String string) {
                LOGGER.info(string);
            }
        };
    }
}
