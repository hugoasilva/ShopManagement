package pt.hugoasilva.shopmanagement.ui.calendar.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRippler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.util.*;

/**
 * Calendar Controller Class
 *
 * @author Hugo Silva
 * @version 2020-11-08
 */

public class CalendarController implements Initializable {

    @FXML
    private Label labelMonth;
    @FXML
    private ImageView janNav, febNav, marNav, aprNav, mayNav, junNav,
            julNav, augNav, sepNav, octNav, novNav, decNav;
    @FXML
    private GridPane gridPane;
    @FXML
    private Label labelYear;

    private String selectedMonth;
    private ArrayList<DayNode> dayList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        hideShowNav(getCurrentMonth()); //get the current month and show only the navigation on it.
        this.selectedMonth =
                LocalDate.now().getMonth().getDisplayName(
                        TextStyle.FULL_STANDALONE,
                        new Locale("pt", "PT"));
        this.labelMonth.setText(this.selectedMonth);
        this.labelYear.setText(String.valueOf(this.getCurrentYear()));

        //Add AnchorPane to GridView
        for (int i = 0; i < 6; i++) { //Row has 6, means we only shows six weeks on calendar, change it to your needs.
            for (int j = 0; j < 7; j++) { //Column has 7, for 7 days a week
                //Layout of AnchorPane
                DayNode dayNode = new DayNode();
                dayNode.setPrefSize(200, 200);
                dayNode.setPadding(new Insets(10));

                JFXRippler rippler = new JFXRippler(dayNode);
                rippler.setRipplerFill(Paint.valueOf("#CCCCCC"));
                gridPane.add(rippler, j, i);

                dayList.add(dayNode); //add the AnchorPane in a list
            }
        }
        populateDate(YearMonth.now());
    }

    /**
     * Populate calendar grid
     **/
    private void populateDate(YearMonth yearMonth) {
        // Get the date we want to start with on the calendar
        LocalDate calendarDate =
                LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
        // Dial back the day until it is SUNDAY (unless the month starts on a sunday)
        while (!calendarDate.getDayOfWeek().toString().equals("SUNDAY")) {
            calendarDate = calendarDate.minusDays(1);
        }
        // Populate the calendar with day numbers
        for (DayNode dayNode : dayList) {
            if (dayNode.getChildren().size() != 0) {
                dayNode.getChildren().clear(); //remove the label in AnchorPane
            }

            dayNode.setDate(calendarDate); //set date into AnchorPane

            Label label = new Label();
            label.setText(String.valueOf(calendarDate.getDayOfMonth()));
            label.setFont(Font.font("Roboto", 16)); //set the font of Text
            label.getStyleClass().add("notInRangeDays");
            if (isDateInRange(yearMonth, dayNode.getDate())) {
                label.getStyleClass().remove("notInRangeDays");
            }
            if (dayNode.getDate().equals(LocalDate.of(yearMonth.getYear(),
                    yearMonth.getMonth(), yearMonth.lengthOfMonth()))) {
                label.getStyleClass().remove("notInRangeDays");
            }
            dayNode.setTopAnchor(label, 5.0);
            dayNode.setLeftAnchor(label, 5.0);
            dayNode.getChildren().add(label);
            dayNode.getStyleClass().remove("selectedDate"); //remove selection on date change
            dayNode.getStyleClass().remove("dateNow"); //remove selection on current date
            if (dayNode.getDate().equals(LocalDate.now())) { //if date is equal to current date now, then add a defualt color to pane
                dayNode.getStyleClass().add("dateNow");
            }
            dayNode.setOnMouseClicked(event -> { //Handle click event of AnchorPane
                for (DayNode day : dayList) {
                    day.getStyleClass().remove("selectedDate");
                }
                dayNode.getStyleClass().add("selectedDate");
            });
            calendarDate = calendarDate.plusDays(1);
        }
    }

    /**
     * Check if day is in range of month
     **/
    private boolean isDateInRange(YearMonth yearMonth, LocalDate currentDate) {
        LocalDate start = LocalDate.of(
                yearMonth.getYear(), yearMonth.getMonth(), 1);
        LocalDate stop = LocalDate.of(
                yearMonth.getYear(), yearMonth.getMonth(), yearMonth.lengthOfMonth());
        return (!currentDate.isBefore(start)) && (currentDate.isBefore(stop));
    }

    /**
     * Change the calendar according to selected month and year
     **/
    private void changeCalendar(int year, String month) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("yyyy MMMM")
                .toFormatter(new Locale("pt", "PT"));
        populateDate(YearMonth.parse(year + " " + month, formatter));
        this.selectedMonth = month;
    }

    /**
     * Month click handler
     *
     * @param event click event
     * @throws ParseException Parse Exception
     */
    @FXML
    private void monthButtonHandler(ActionEvent event) throws ParseException {
        JFXButton monthButton = (JFXButton) event.getSource();
        String month = monthButton.getText();
        Date date = new SimpleDateFormat("MMM",
                new Locale("pt", "PT")).parse(month);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        System.out.println(cal.get(Calendar.MONTH));
        this.labelMonth.setText(month);
        hideShowNav(cal.get(Calendar.MONTH));
        changeCalendar(Integer.parseInt(this.labelYear.getText()), month);
    }

    /**
     * Previous year button click handler
     *
     * @param event button click
     */
    @FXML
    private void previousYearButtonHandler(ActionEvent event) {
        this.labelYear.setText(
                String.valueOf(Integer.parseInt(this.labelYear.getText()) - 1));
        this.changeCalendar(
                Integer.parseInt(this.labelYear.getText()), this.selectedMonth);
    }

    /**
     * Next year button click handler
     *
     * @param event button click
     */
    @FXML
    private void nextYearButtonHandler(ActionEvent event) {
        this.labelYear.setText(String.valueOf(Integer.parseInt(this.labelYear.getText()) + 1));
        this.changeCalendar(Integer.parseInt(this.labelYear.getText()), this.selectedMonth);
    }

    /**
     * Get current month
     **/
    private int getCurrentMonth() {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        if (month > 0) {
            return month - 1;
        }
        return month;
    }

    /**
     * Get current year
     **/
    private int getCurrentYear() {
        LocalDate today = LocalDate.now();
        return today.getYear();
    }

    /**
     * Hide/show month navigation
     **/
    private void hideShowNav(int month) {
        this.janNav.setVisible(false);
        this.febNav.setVisible(false);
        this.marNav.setVisible(false);
        this.aprNav.setVisible(false);
        this.mayNav.setVisible(false);
        this.junNav.setVisible(false);
        this.julNav.setVisible(false);
        this.augNav.setVisible(false);
        this.sepNav.setVisible(false);
        this.octNav.setVisible(false);
        this.novNav.setVisible(false);
        this.decNav.setVisible(false);

        switch (month) {
            case 0:
                this.janNav.setVisible(true);
                break;
            case 1:
                this.febNav.setVisible(true);
                break;
            case 2:
                this.marNav.setVisible(true);
                break;
            case 3:
                this.aprNav.setVisible(true);
                break;
            case 4:
                this.mayNav.setVisible(true);
                break;
            case 5:
                this.junNav.setVisible(true);
                break;
            case 6:
                this.julNav.setVisible(true);
                break;
            case 7:
                this.augNav.setVisible(true);
                break;
            case 8:
                this.sepNav.setVisible(true);
                break;
            case 9:
                this.octNav.setVisible(true);
                break;
            case 10:
                this.novNav.setVisible(true);
                break;
            case 11:
                this.decNav.setVisible(true);
                break;
            default:
                break;
        }
    }
}