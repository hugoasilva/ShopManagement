package pt.shop.management.data.files;

import com.google.gson.Gson;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.shop.management.data.model.Note;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * JSON Handler Class
 *
 * @author Hugo Silva
 * @version 2020-10-23
 */

public class JSONHandler {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(JSONHandler.class.getName());

    public static boolean notesToJSON(List<Note> notes, String path) {
        try {
            // Create Gson instance
            Gson gson = new Gson();

            // Create a writer
            Writer writer = Files.newBufferedWriter(Paths.get(path));

            // Convert notes list object to JSON file
            gson.toJson(notes, writer);

            // Close writer
            writer.close();
            return true;
        } catch (Exception ex) {
            printJSONException(ex);
            return false;
        }
    }

    public static List<Note> JSONToNotes(String path) {
        List<Note> notes = new ArrayList<>();
        try {
            // Create Gson instance
            Gson gson = new Gson();

            // Create a reader
            Reader reader = Files.newBufferedReader(Paths.get(path));

            // Convert JSON array to list of notes
            notes = Arrays.asList(gson.fromJson(reader, Note[].class));

            // Close reader
            reader.close();
        } catch (Exception ex) {
            notes.add(new Note("0", "error"));
            printJSONException(ex);
        }
        return notes;
    }

    /**
     * Log JSON exception
     *
     * @param e - JSON exception
     */
    private static void printJSONException(Exception e) {
        if (e != null) {
            e.printStackTrace(System.err);
            LOGGER.log(Level.ERROR, "{}", "JSON Error: " + e.getMessage());
            Throwable t = e.getCause();
            while (t != null) {
                LOGGER.log(Level.ERROR, "{}", "Cause: " + t);
                t = t.getCause();
            }
        }
    }
}

