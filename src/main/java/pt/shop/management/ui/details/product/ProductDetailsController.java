package main.java.pt.shop.management.ui.details.product;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.java.pt.shop.management.data.database.DatabaseHandler;
import main.java.pt.shop.management.data.model.Product;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Product Details Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-13
 */

public class ProductDetailsController implements Initializable {

    // Database query
    private static final String SELECT_PRODUCT_QUERY = "SELECT * FROM produtos WHERE id_produto=?";

    // Customer data
    private final String productID;

    // Database handler instance

    DatabaseHandler databaseHandler;
    // UI Content
    @FXML
    private Label id;
    @FXML
    private Label name;
    @FXML
    private Label price;
    @FXML
    private Label quantity;

    public ProductDetailsController(String id) {
        this.productID = id;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
        try {
            loadData();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Cancel button handler
     *
     * @param event - cancel event
     */
    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }

    /**
     * Populate interface
     *
     * @param product - product object
     */
    public void inflateUI(Product product) {
        id.setText("ID: " + product.getId());
        name.setText("Nome: " + product.getName());
        price.setText("Preço: " + product.getPrice());
        quantity.setText("Quantidade: " + product.getQuantity());
    }

    /**
     * Load product details data
     *
     * @throws SQLException - database exception
     */
    private void loadData() throws SQLException {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        PreparedStatement preparedStatement = handler.getConnection().prepareStatement(SELECT_PRODUCT_QUERY);
        preparedStatement.setString(1, productID);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        String id = resultSet.getString("id_produto");
        String name = resultSet.getString("nome");
        String price = resultSet.getString("preco");
        String quantity = resultSet.getString("quantidade");
        this.inflateUI(new Product(id, name, price, quantity));
    }
}
