package pt.shop.management.exceptions;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Exception Handler Class
 *
 * @author Hugo Silva
 * @version 2020-10-13
 */

public class DefaultExceptionHandler implements UncaughtExceptionHandler {

    private final static Logger LOGGER = LogManager.getLogger(DefaultExceptionHandler.class.getName());

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LOGGER.log(Level.ERROR, "Exception occurred {}", ex);
    }
}
