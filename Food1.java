import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class Food extends Applet implements ActionListener {
    Button b1, b2;
    Panel menuPanel, detailPanel, buttonPanel;
    Label priceLabel;
    DecimalFormat currency = new DecimalFormat("0.00");

    public void init() {
        setLayout(new BorderLayout());

        // Initialize the button panel
        buttonPanel = new Panel();
        b1 = new Button("Dine In");
        b2 = new Button("Take Away");

        b1.addActionListener(this);
        b2.addActionListener(this);

        buttonPanel.add(b1);
        buttonPanel.add(b2);

        // Initialize the menu panel
        menuPanel = new Panel();
        menuPanel.setLayout(new GridLayout(2, 2, 10, 10)); // 2x2 grid

        // Initialize the detail panel
        detailPanel = new Panel(new BorderLayout());

        // Add the button and menu panels to the applet
        add(buttonPanel, BorderLayout.NORTH);
        add(menuPanel, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == b1 || e.getSource() == b2) {
            showMenuPage();
        } else if (e.getActionCommand().equals("Small")) {
            updatePrice(8.0);
        } else if (e.getActionCommand().equals("Medium")) {
            updatePrice(9.0);
        } else if (e.getActionCommand().equals("Large")) {
            updatePrice(10.0);
        }
    }

    private void showMenuPage() {
        // Clear previous items in the menu panel
        menuPanel.removeAll();

        // Sample food items
        String[] foodNames = {"Burger", "Pizza", "Sushi", "Pasta"};
        for (String food : foodNames) {
            addFoodItem(food);
        }

        // Refresh the applet
        validate();
        repaint();
    }

    private void showFoodDetail(String foodName) {
        // Remove existing panels
        remove(menuPanel);
        remove(buttonPanel);

        // Clear previous items in the detail panel
        detailPanel.removeAll();

        // Display food details
        displayFoodDetail(foodName);

        // Add the detail panel to the applet
        add(detailPanel, BorderLayout.CENTER);

        // Refresh the applet
        validate();
        repaint();
    }

    private void addFoodItem(final String foodName) {
        Panel foodPanel = new Panel(new BorderLayout());
        Label nameLabel = new Label(foodName, Label.CENTER);
        Button selectButton = new Button("Select");

        selectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showFoodDetail(foodName);
            }
        });

        foodPanel.add(nameLabel, BorderLayout.CENTER);
        foodPanel.add(selectButton, BorderLayout.SOUTH);

        menuPanel.add(foodPanel);
    }

    private void displayFoodDetail(String foodName) {
        // Title label
        Label titleLabel = new Label(foodName, Label.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.magenta);
        detailPanel.add(titleLabel, BorderLayout.NORTH);

        // Price label
        priceLabel = new Label("RM0.00", Label.CENTER);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 60));
        priceLabel.setForeground(Color.blue);
        detailPanel.add(priceLabel, BorderLayout.CENTER);

        // Panel for size selection buttons
        Panel sizePanel = new Panel(new FlowLayout());
        sizePanel.setBackground(Color.pink);

        Button b3 = new Button("Small");
        Button b4 = new Button("Medium");
        Button b5 = new Button("Large");

        b3.addActionListener(this);
        b4.addActionListener(this);
        b5.addActionListener(this);

        sizePanel.add(b3);
        sizePanel.add(b4);
        sizePanel.add(b5);

        detailPanel.add(sizePanel, BorderLayout.SOUTH);
    }

    private void updatePrice(double amount) {
        priceLabel.setText("RM" + currency.format(amount));
    }
}
