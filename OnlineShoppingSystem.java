import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

class Product {
    private int id;
    private String name;
    private String description;
    private double price;

    // Constructor
    public Product(int id, String name, String description, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }
}

class ShoppingCart {
    private List<Product> items;

    // Constructor
    public ShoppingCart() {
        items = new ArrayList<>();
    }

    // Add product to cart
    public void addProduct(Product product) {
        items.add(product);
    }

    // Remove product from cart
    public void removeProduct(Product product) {
        items.remove(product);
    }

    // Get items in cart
    public List<Product> getItems() {
        return items;
    }

    // Calculate total price
    public double calculateTotal() {
        double total = 0;
        for (Product item : items) {
            total += item.getPrice();
        }
        return total;
    }

    // Clear cart after checkout
    public void clearCart() {
        items.clear();
    }
}

public class OnlineShoppingSystem extends Application {
    private ShoppingCart cart;

    @Override
    public void start(Stage primaryStage) {
        // Create some sample products
        Product product1 = new Product(1, "Product 1", "Description 1", 10.99);
        Product product2 = new Product(2, "Product 2", "Description 2", 15.99);

        // Create shopping cart
        cart = new ShoppingCart();

        // Add products to cart
        cart.addProduct(product1);
        cart.addProduct(product2);

        // Create UI components
        Label cartLabel = new Label("Items in the shopping cart:");
        ListView<String> cartListView = new ListView<>();
        cartListView.setPrefHeight(100);
        updateCartListView(cartListView);

        Label totalLabel = new Label("Total price: $" + cart.calculateTotal());
        Button removeButton = new Button("Remove Selected Item");
        Button checkoutButton = new Button("Checkout");

        // Handle remove button click
        removeButton.setOnAction(e -> {
            String selectedItem = cartListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                cart.removeProduct(getProductByName(selectedItem));
                updateCartListView(cartListView);
                totalLabel.setText("Total price: $" + cart.calculateTotal());
            }
        });

        // Handle checkout button click
        checkoutButton.setOnAction(e -> {
            // Simulate placing an order
            cart.clearCart();
            updateCartListView(cartListView);
            totalLabel.setText("Total price: $" + cart.calculateTotal());
            showAlert("Order Placed", "Thank you for your purchase!");
        });

        // Create layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(cartLabel, cartListView, totalLabel, removeButton, checkoutButton);

        // Create scene
        Scene scene = new Scene(layout, 300, 250);

        // Set stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Online Shopping System");
        primaryStage.show();
    }

    // Method to update cart ListView
    private void updateCartListView(ListView<String> listView) {
        listView.getItems().clear();
        for (Product item : cart.getItems()) {
            listView.getItems().add(item.getName());
        }
    }

    // Method to get product by name
    private Product getProductByName(String name) {
        for (Product product : cart.getItems()) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }

    // Method to show an alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
