import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.Vector;

public class FoodOrderingApp extends Applet implements ActionListener {
    private Button dineInBtn, takeAwayBtn;
    private Panel menuPanel, detailPanel, cartPanel, buttonPanel;
    private Vector<CartItem> cartItems;
    private DecimalFormat currency = new DecimalFormat("0.00");
    private Label priceLabel;
    private String orderType;

    class CartItem {
        String name;
        double price;

        public CartItem(String name, double price) {
            this.name = name;
            this.price = price;
        }
    }

    public void init() {
        setLayout(new BorderLayout());
        cartItems = new Vector<>();

        // Initialize button panel for Dine In / Take Away
        buttonPanel = new Panel();
        dineInBtn = new Button("Dine In");
        takeAwayBtn = new Button("Take Away");
        dineInBtn.addActionListener(this);
        takeAwayBtn.addActionListener(this);
        buttonPanel.add(dineInBtn);
        buttonPanel.add(takeAwayBtn);
        add(buttonPanel, BorderLayout.NORTH);

        // Initialize menu panel and cart panel
        menuPanel = new Panel(new GridLayout(2, 2, 10, 10));
        cartPanel = new Panel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
        add(cartPanel, BorderLayout.EAST);

        // Detail panel for displaying food details and payment
        detailPanel = new Panel();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == dineInBtn || e.getSource() == takeAwayBtn) {
            orderType = ((Button) e.getSource()).getLabel();
            showCategorySelection();
        }
    }

    private void showCategorySelection() {
        menuPanel.removeAll();
        Button foodBtn = new Button("Food");
        Button beverageBtn = new Button("Beverage");

        foodBtn.addActionListener(e -> showItems("Food"));
        beverageBtn.addActionListener(e -> showItems("Beverage"));

        menuPanel.add(foodBtn);
        menuPanel.add(beverageBtn);
        add(menuPanel, BorderLayout.CENTER);
        validate();
    }

    private void showItems(String category) {
        menuPanel.removeAll();
        String[] items = category.equals("Food")
                ? new String[]{"Burger", "Pizza", "Sushi", "Pasta"}
                : new String[]{"Cola", "Juice", "Coffee", "Tea"};
        
        for (String item : items) {
            addMenuItem(item);
        }
        menuPanel.validate();
        add(menuPanel, BorderLayout.CENTER);
        validate();
    }

    private void addMenuItem(final String itemName) {
        Panel itemPanel = new Panel(new BorderLayout());
        Label nameLabel = new Label(itemName, Label.CENTER);
        Button selectButton = new Button("Select");

        selectButton.addActionListener(e -> addToCart(itemName));

        itemPanel.add(nameLabel, BorderLayout.NORTH);
        itemPanel.add(selectButton, BorderLayout.SOUTH);
        menuPanel.add(itemPanel);
    }

    private void addToCart(String itemName) {
        double price = switch (itemName) {
            case "Burger" -> 5.99;
            case "Pizza" -> 8.99;
            case "Sushi" -> 10.99;
            case "Pasta" -> 7.99;
            case "Cola" -> 1.99;
            case "Juice" -> 2.99;
            case "Coffee" -> 2.49;
            case "Tea" -> 1.49;
            default -> 0.0;
        };

        cartItems.add(new CartItem(itemName, price));
        updateCartDisplay();
    }

    private void updateCartDisplay() {
        cartPanel.removeAll();
        double total = 0.0;
        for (CartItem item : cartItems) {
            Panel itemPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
            itemPanel.add(new Label(item.name + " - RM" + currency.format(item.price)));
            cartPanel.add(itemPanel);
            total += item.price;
        }
        cartPanel.add(new Label("Total: RM" + currency.format(total)));
        Button checkoutBtn = new Button("Checkout");

        checkoutBtn.addActionListener(e -> showPaymentPage());

        cartPanel.add(checkoutBtn);
        cartPanel.validate();
        cartPanel.repaint();
    }

    private void showPaymentPage() {
        remove(menuPanel);
        detailPanel.removeAll();
        detailPanel.setLayout(new GridLayout(4, 2));
        
        detailPanel.add(new Label("Card Number:"));
        TextField cardNumber = new TextField();
        detailPanel.add(cardNumber);
        
        detailPanel.add(new Label("Expiry (MM/YY):"));
        TextField expiry = new TextField();
        detailPanel.add(expiry);
        
        detailPanel.add(new Label("CVV:"));
        TextField cvv = new TextField();
        detailPanel.add(cvv);
        
        Button submitBtn = new Button("Submit Payment");
        submitBtn.addActionListener(e -> showConfirmation());
        detailPanel.add(submitBtn);

        add(detailPanel, BorderLayout.CENTER);
        validate();
        repaint();
    }

    private void showConfirmation() {
        detailPanel.removeAll();
        detailPanel.add(new Label("Thank you for your " + orderType + " order!"));
        cartItems.clear();
        updateCartDisplay();
        validate();
        repaint();
    }
}
