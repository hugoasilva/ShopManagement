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
import java.util.Arrays;
import java.util.List;

/**
 * JSON Handler Class
 *
 * @author Hugo Silva
 * @version 2020-10-22
 */

public class JSONHandler {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(JSONHandler.class.getName());

    public static void notesToJSON(List<Note> notes, String path) {
        try {
//            // Initialize a list of type DataObject
//            List<Note> objList = new ArrayList<>();
//            objList.add(new Note("0", "zero"));
//            objList.add(new Note("1", "one"));
//            objList.add(new Note("2", "two"));

            // create Gson instance
            Gson gson = new Gson();

            // create a writer
            Writer writer = Files.newBufferedWriter(Paths.get(path));

            // convert book object to JSON file
            gson.toJson(notes, writer);

            // close writer
            writer.close();

        } catch (Exception ex) {
            printJSONException(ex);
        }
    }

    public static Object JSONToNotes(String path) {
        try {
            // create Gson instance
            Gson gson = new Gson();

            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get(path));

            // convert JSON array to list of books
            List<Note> notes = Arrays.asList(gson.fromJson(reader, Note[].class));

            // print books
            for (Note x : notes) {
                System.out.println(x.getMessage());
            }

            // close reader
            reader.close();

            return notes;
        } catch (Exception ex) {
            printJSONException(ex);
            return false;
        }
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

