package pt.shop.management.ui.search.invoice;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.files.SFTPHandler;
import pt.shop.management.data.model.Invoice;
import pt.shop.management.ui.add.invoice.InvoiceAddController;
import pt.shop.management.ui.alert.AlertMaker;
import pt.shop.management.ui.details.invoice.InvoiceDetailsController;
import pt.shop.management.util.ShopManagementUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Invoice Search Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-23
 */

public class InvoiceSearchController implements Initializable {

    // Database queries
    private static final String SEARCH_ID_QUERY = "SELECT * FROM faturas WHERE id_fatura=?";
    private static final String SEARCH_CUSTOMER_QUERY = "SELECT * FROM faturas WHERE id_cliente=?";
    private static final String SEARCH_EMPLOYEE_QUERY = "SELECT * FROM faturas WHERE id_empregado=?";
    private static final String SEARCH_DATE_QUERY = "SELECT * FROM faturas WHERE data_fatura=?";

    private final static String LOCAL_DOWNLOAD_PATH = "downloads/";
    private final String type;
    private final String search;
    // Invoice list object
    ObservableList<Invoice> list = FXCollections.observableArrayList();
    // UI Content
    @FXML
    private StackPane rootPane;
    @FXML
    private TableView<Invoice> tableView;
    @FXML
    private TableColumn<Invoice, String> idCol;
    @FXML
    private TableColumn<Invoice, String> customerCol;
    @FXML
    private TableColumn<Invoice, String> employeeCol;
    @FXML
    private TableColumn<Invoice, String> dateCol;
    @FXML
    private TableColumn<Invoice, String> productsCol;
    @FXML
    private AnchorPane contentPane;

    public InvoiceSearchController(String type, String search) {
        this.type = type;
        this.search = search;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        try {
            loadData();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * Assign table columns to invoice properties
     */
    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        employeeCol.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        productsCol.setCellValueFactory(new PropertyValueFactory<>("products"));
        TableColumn<Invoice, Void> pdfCol = new TableColumn<>("PDF");

        Callback<TableColumn<Invoice, Void>, TableCell<Invoice, Void>> cellFactoryPDF =
                new Callback<>() {
                    @Override
                    public TableCell<Invoice, Void> call(final TableColumn<Invoice, Void> param) {
                        return new TableCell<>() {
                            private final Button btn = new Button("Abrir PDF");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    Invoice data = getTableView().getItems().get(getIndex());
                                    showInvoicePDF(data.getId(), data.getPdf());
                                });
                            }

                            @Override
                            public void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(btn);
                                }
                            }
                        };
                    }
                };
        TableColumn<Invoice, Void> detailsCol = new TableColumn<>("Ficha");
        Callback<TableColumn<Invoice, Void>, TableCell<Invoice, Void>> cellFactoryDetails =
                new Callback<>() {
                    @Override
                    public TableCell<Invoice, Void> call(final TableColumn<Invoice, Void> param) {
                        return new TableCell<>() {
                            private final Button btn = new Button("Abrir Ficha");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    Invoice data = getTableView().getItems().get(getIndex());
                                    try {
                                        showInvoiceDetails(data.getId());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                            }

                            @Override
                            public void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(btn);
                                }
                            }
                        };
                    }
                };

        pdfCol.setCellFactory(cellFactoryPDF);
        detailsCol.setCellFactory(cellFactoryDetails);
        tableView.getColumns().add(pdfCol);
        tableView.getColumns().add(detailsCol);
    }

    /**
     * Show Invoice PDF
     *
     * @param pdfPath - invoice pdf file path
     */
    private void showInvoicePDF(String id, String pdfPath) {
        String fileName = id + ".pdf";
        SFTPHandler.downloadFile(pdfPath, fileName);

        // Open file
        ShopManagementUtil.openFile(LOCAL_DOWNLOAD_PATH + fileName);
    }

    private void showInvoiceDetails(String id) throws IOException {
        InvoiceDetailsController controller = new InvoiceDetailsController(id);

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource(
                        "/fxml/invoice/InvoiceDetails.fxml"));
        loader.setController(controller);

        Parent parent = loader.load();

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Ficha de Fatura");
        stage.setScene(new Scene(parent));
        stage.show();
        ShopManagementUtil.setStageIcon(stage);
    }

    private Stage getStage() {
        return (Stage) tableView.getScene().getWindow();
    }

    /**
     * Load invoice search table data
     *
     * @throws SQLException - database exception
     */
    private void loadData() throws SQLException {
        String query = null;
        switch (this.type) {
            case "ID":
                query = SEARCH_ID_QUERY;
                break;
            case "ID Cliente":
                query = SEARCH_CUSTOMER_QUERY;
                break;
            case "ID Empregado":
                query = SEARCH_EMPLOYEE_QUERY;
                break;
            case "Data":
                query = SEARCH_DATE_QUERY;
                break;
        }
        list.clear();
        DatabaseHandler handler = DatabaseHandler.getInstance();
        PreparedStatement preparedStatement = handler.getConnection().prepareStatement(query);
        preparedStatement.setString(1, this.search);
        ResultSet resultSet = preparedStatement.executeQuery();
        try {
            while (resultSet.next()) {
                String id = resultSet.getString("id_fatura");
                String customerId = resultSet.getString("id_cliente");
                String employeeId = resultSet.getString("id_empregado");
                String date = resultSet.getString("data_fatura");
                String products = resultSet.getString("produtos");
                String pdf = resultSet.getString("pdf");

                list.add(new Invoice(id, customerId, employeeId, date, products, pdf));
            }
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceSearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableView.setItems(list);
    }

    /**
     * Invoice delete handler
     *
     * @param event - delete event
     */
    @FXML
    private void handleInvoiceDelete(ActionEvent event) {
        //Fetch the selected row
        Invoice selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            AlertMaker.showErrorMessage("Nenhuma fatura seleccionada",
                    "Por favor seleccione uma fatura para apagar.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Apagar Fatura");
        alert.setContentText("Tem a certeza que pretende apagar a fatura nr " + selectedForDeletion.getId() + " ?");
        Optional<ButtonType> answer = alert.showAndWait();

        if (answer.get() == ButtonType.OK) {
            Boolean result = DatabaseHandler.getInstance().deleteInvoice(selectedForDeletion);
            if (result) {
                AlertMaker.showSimpleAlert("Fatura apagada", "Fatura nr " + selectedForDeletion.getId() +
                        " apagada com sucesso.");
                list.remove(selectedForDeletion);
            } else {
                AlertMaker.showSimpleAlert("Erro!",
                        new String("Não foi possível apagar a fatura nr ".getBytes(), StandardCharsets.UTF_8)
                                + selectedForDeletion.getId());
            }
        } else {
            AlertMaker.showSimpleAlert("Cancelado",
                    new String("Nenhuns dados serão apagados.".getBytes(), StandardCharsets.UTF_8));
        }
    }

    /**
     * Refresh handler
     *
     * @param event - refresh event
     * @throws SQLException - database exception
     */
    @FXML
    private void handleRefresh(ActionEvent event) throws SQLException {
        loadData();
    }

    /**
     * Invoice edit handler
     *
     * @param event - edit event
     */
    @FXML
    private void handleInvoiceEdit(ActionEvent event) {
        //Fetch the selected row
        Invoice selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if (selectedForEdit == null) {
            AlertMaker.showErrorMessage("Nenhuma fatura seleccionada",
                    "Por favor seleccione uma fatura para editar.");
            return;
        }
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/invoice/InvoiceAdd.fxml"));
            Parent parent = loader.load();

            InvoiceAddController controller = loader.getController();
            controller.inflateUI(selectedForEdit);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Editar Fatura");
            stage.setScene(new Scene(parent));
            stage.show();
            ShopManagementUtil.setStageIcon(stage);

            stage.setOnHiding((e) -> {
                try {
                    handleRefresh(new ActionEvent());
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(InvoiceSearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Close current window
     *
     * @param event - close event
     */
    @FXML
    private void closeStage(ActionEvent event) {
        getStage().close();
    }
}
