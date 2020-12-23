package pt.hugoasilva.shopmanagement.ui.calendar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;

public class CalendarLoader extends Application {

    final String title = "Calendário";
    final int defaultWidth = 650;
    final int defaultHeight = 568;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/calendar/Calendar.fxml"));
        primaryStage.setTitle(title);
        primaryStage.getIcons().add(new Image(CalendarLoader.class.getResourceAsStream("/images/icon_calendar.png")));
        Scene scene = new Scene(root, defaultWidth, defaultHeight);
        URL resource = getClass().getResource("/stylesheet/stylesheet_main.css");
        scene.getStylesheets().add(String.valueOf(resource));
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}