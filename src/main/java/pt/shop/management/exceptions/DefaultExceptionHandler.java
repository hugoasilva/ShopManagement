package pt.shop.management.exceptions;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Exception Handler Class
 *
 * @author Hugo Silva
 * @version 2020-11-01
 */

public class DefaultExceptionHandler implements UncaughtExceptionHandler {

    private final static Logger LOGGER = LogManager.getLogger(DefaultExceptionHandler.class.getName());

    /**
     * Log uncaught exception
     *
     * @param thread exception's thread
     * @param ex     exception
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LOGGER.log(Level.ERROR, "Exception occurred {}", ex);
    }
}
