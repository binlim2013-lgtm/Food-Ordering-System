import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.io.*;


   public class Food extends Applet implements ActionListener, ItemListener {
	private Button b1, b2, b3, b4, b5, confirmButton, backButton, paymentButton, printReceiptButton, saveReceiptButton, loadReceiptButton, historyButton;
    	private Panel menuPanel, detailPanel, buttonPanel, cartPanel, paymentPanel, receiptPanel, historyPanel;
	    private Label priceLabel;
    	private Choice quantityChoice;
    	private double selectedPrice = 0.0;
    	private DecimalFormat currency = new DecimalFormat("0.00");
		private Image foodImage;

    	private ArrayList<String> cartItems = new ArrayList<String>();
    	private ArrayList<Integer> cartQuantities = new ArrayList<Integer>();
    	private ArrayList<Double> cartPrices = new ArrayList<Double>();
    	private String currentItem = "";

    	private double totalAmount = 0;

    	
public void init() {
    	setLayout(new BorderLayout());
	setBackground(Color.PINK);

    	buttonPanel = new Panel();
    	b1 = new Button("Dine In");
    	b2 = new Button("Take Away");

   	b1.addActionListener(this);
   	b2.addActionListener(this);

    	buttonPanel.add(b1);
    	buttonPanel.add(b2);

    // Initialize buttons globally
    	confirmButton = new Button("Confirm");
    	backButton = new Button("Back to Menu");
    	paymentButton = new Button("Proceed to Payment");
    	printReceiptButton = new Button("Print Receipt");
    	saveReceiptButton = new Button("Save Receipt");
    	loadReceiptButton = new Button("Load Receipt");
    	historyButton = new Button("View History");

    	confirmButton.addActionListener(this);
    	backButton.addActionListener(this);
    	paymentButton.addActionListener(this);
    	printReceiptButton.addActionListener(this);
    	saveReceiptButton.addActionListener(this);
    	loadReceiptButton.addActionListener(this);
    	historyButton.addActionListener(this);

    	menuPanel = new Panel();
    	menuPanel.setLayout(new GridLayout(2, 2, 10, 10));

    	detailPanel = new Panel(new BorderLayout());
    	cartPanel = new Panel(new GridBagLayout());
    	paymentPanel = new Panel(new GridBagLayout());
    	receiptPanel = new Panel(new GridBagLayout());
    	historyPanel = new Panel(new GridBagLayout());

    	add(buttonPanel, BorderLayout.NORTH);
    	add(menuPanel, BorderLayout.CENTER);
   }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == b1 || e.getSource() == b2) {
            showMenuPage();
        } else if (e.getSource() == confirmButton) {
            addToCart();
            showCartPage();
        } else if (e.getSource() == backButton) {
            showMenuPage();
        } else if (e.getSource() == paymentButton) {
            showPaymentPage();
        } else if (e.getSource() == printReceiptButton) {
            showReceiptPage();
        } else if (e.getSource() == saveReceiptButton) {
            saveReceiptToFile();
        } else if (e.getSource() == loadReceiptButton) {
            loadReceiptFromFile();
 	} else if (e.getSource() == historyButton) {
            showHistoryPage();
        }
    }

    private void showMenuPage() {
        remove(cartPanel);
        remove(paymentPanel);
        remove(receiptPanel);
	    remove(historyPanel);
        detailPanel.removeAll();
        menuPanel.removeAll();

        String[] foodNames = {"Burger", "Pizza", "Sushi", "Pasta"};
        for (String food : foodNames) {
            addFoodItem(food);
        }

        add(menuPanel, BorderLayout.CENTER);

        validate();
        repaint();
    }

    private void showFoodDetail(String foodName) {
        remove(menuPanel);
        remove(buttonPanel);

        detailPanel.removeAll();

        currentItem = foodName;
        displayFoodDetail(foodName);

        add(detailPanel, BorderLayout.CENTER);

        validate();
        repaint();
    }

    private void addFoodItem(final String foodName) {
    Panel foodPanel = new Panel(new BorderLayout());
    
    // Declare the final reference for use in the canvas
    final Image foodImage = getImage(getCodeBase(), foodName.toLowerCase() + ".jpg");

    Canvas imageCanvas = new Canvas() {
        public void paint(Graphics g) {
            // Draw image with larger size
            g.drawImage(foodImage, 10, 10, 160, 160, this);
        }
    };
    imageCanvas.setSize(180, 180);

    // Food name label below the image
    Label nameLabel = new Label(foodName, Label.CENTER);
    nameLabel.setFont(new Font("Arial", Font.BOLD, 16));

    // Button for selecting
    Button selectButton = new Button("Select");
    selectButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            showFoodDetail(foodName);
        }
    });

    // Add components to the panel
    foodPanel.setLayout(new BorderLayout());
    foodPanel.add(imageCanvas, BorderLayout.CENTER);
    foodPanel.add(nameLabel, BorderLayout.NORTH);
    foodPanel.add(selectButton, BorderLayout.SOUTH);

    menuPanel.add(foodPanel);
}


    private void displayFoodDetail(String foodName) {
        Label titleLabel = new Label(foodName, Label.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.magenta);
        detailPanel.add(titleLabel, BorderLayout.NORTH);

        priceLabel = new Label("RM0.00", Label.CENTER);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 60));
        priceLabel.setForeground(Color.blue);
        detailPanel.add(priceLabel, BorderLayout.CENTER);

        Panel sizePanel = new Panel(new FlowLayout());
        sizePanel.setBackground(Color.pink);

        b3 = new Button("Small");
        b4 = new Button("Medium");
        b5 = new Button("Large");

        b3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updatePrice(currentItem, "Small");
            }
        });

        b4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updatePrice(currentItem, "Medium");
            }
        });

        b5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updatePrice(currentItem, "Large");
            }
        });

        sizePanel.add(b3);
        sizePanel.add(b4);
        sizePanel.add(b5);

        quantityChoice = new Choice();
        for (int i = 1; i <= 10; i++) {
            quantityChoice.add(String.valueOf(i));
        }
        quantityChoice.addItemListener(this);

        sizePanel.add(new Label("Quantity:"));
        sizePanel.add(quantityChoice);

        confirmButton = new Button("Confirm");
        confirmButton.addActionListener(this);
        sizePanel.add(confirmButton);

        detailPanel.add(sizePanel, BorderLayout.SOUTH);
    }

    private void updatePrice(String foodName, String size) {
        if (foodName.equals("Burger")) {
            if (size.equals("Small")) {
                selectedPrice = 5.0;
            } else if (size.equals("Medium")) {
                selectedPrice = 8.0;
            } else if (size.equals("Large")) {
                selectedPrice = 10.0;
            } else {
                selectedPrice = 0.0;
            }
        } else if (foodName.equals("Pizza")) {
            if (size.equals("Small")) {
                selectedPrice = 4.0;
            } else if (size.equals("Medium")) {
                selectedPrice = 8.5;
            } else if (size.equals("Large")) {
                selectedPrice = 10.0;
            } else {
                selectedPrice = 0.0;
            }
        } else if (foodName.equals("Pasta")) {
            if (size.equals("Small")) {
                selectedPrice = 4.0;
            } else if (size.equals("Medium")) {
                selectedPrice = 5.0;
            } else if (size.equals("Large")) {
                selectedPrice = 8.0;
            } else {
                selectedPrice = 0.0;
            }
        } else if (foodName.equals("Sushi")) {
            if (size.equals("Small")) {
                selectedPrice = 3.0;
            } else if (size.equals("Medium")) {
                selectedPrice = 5.0;
            } else if (size.equals("Large")) {
                selectedPrice = 8.0;
            } else {
                selectedPrice = 0.0;
            }
        }
        recalculatePrice();
    }

    private void recalculatePrice() {
        int selectedQuantity = quantityChoice.getSelectedIndex() + 1;
        double totalPrice = selectedPrice * selectedQuantity;
        priceLabel.setText("RM" + currency.format(totalPrice));
    }

    private void addToCart() {
    // Clear the cart before adding a new order
    cartItems.clear();
    cartQuantities.clear();
    cartPrices.clear();
    
    int selectedQuantity = quantityChoice.getSelectedIndex() + 1;
    double totalPrice = selectedPrice * selectedQuantity;

    String sizeLabel = getSizeLabel(selectedPrice);
    if (selectedPrice > 0 && selectedQuantity > 0) {
        cartItems.add(currentItem + " (" + sizeLabel + ")");
        cartQuantities.add(selectedQuantity);
        cartPrices.add(totalPrice);
    }
}


    private String getSizeLabel(double price) {
        if (price == 5.0 || price == 4.0 || price == 3.0) return "S";  // Small
        if (price == 8.0 || price == 8.5 || price == 5.0) return "M";  // Medium
        if (price == 10.0) return "B";  // Big
        return "?";  // Fallback for unknown cases
    }

    private void showCartPage() {
        remove(detailPanel);
        remove(paymentPanel);
        cartPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        Label cartTitle = new Label("Cart", Label.CENTER);
        cartTitle.setFont(new Font("Arial", Font.BOLD, 24));
        cartTitle.setForeground(Color.magenta);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        cartPanel.add(cartTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        cartPanel.add(new Label("Item"), gbc);

        gbc.gridx++;
        cartPanel.add(new Label("Quantity"), gbc);

        gbc.gridx++;
        cartPanel.add(new Label("Price"), gbc);

        totalAmount = 0;
        for (int i = 0; i < cartItems.size(); i++) {
            gbc.gridy++;
            gbc.gridx = 0;
            cartPanel.add(new Label(cartItems.get(i)), gbc);

            gbc.gridx++;
            cartPanel.add(new Label(String.valueOf(cartQuantities.get(i))), gbc);

            gbc.gridx++;
            cartPanel.add(new Label("RM" + currency.format(cartPrices.get(i))), gbc);

            totalAmount += cartPrices.get(i);
        }

        gbc.gridy++;
        gbc.gridx = 0;
        cartPanel.add(new Label("Total:"), gbc);

        gbc.gridx = 2;
        cartPanel.add(new Label("RM" + currency.format(totalAmount)), gbc);

        paymentButton = new Button("Proceed to Payment");
        paymentButton.addActionListener(this);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        cartPanel.add(paymentButton, gbc);

        backButton = new Button("Back to Menu");
        backButton.addActionListener(this);

        gbc.gridy++;
        cartPanel.add(backButton, gbc);

        add(cartPanel, BorderLayout.CENTER);

        validate();
        repaint();
    }

    private void showPaymentPage() {
        remove(cartPanel);
        paymentPanel.removeAll();
        paymentPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        Label paymentTitle = new Label("Payment Page", Label.CENTER);
        paymentTitle.setFont(new Font("Arial", Font.BOLD, 24));
        paymentTitle.setForeground(Color.magenta);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        paymentPanel.add(paymentTitle, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        paymentPanel.add(new Label("Total Amount:"), gbc);

        gbc.gridx = 1;
        paymentPanel.add(new Label("RM" + currency.format(totalAmount)), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        paymentPanel.add(new Label("Card Number:"), gbc);

        TextField cardNumber = new TextField(16);
        gbc.gridx = 1;
        paymentPanel.add(cardNumber, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        paymentPanel.add(new Label("Expiry (MM/YY):"), gbc);

        TextField expiryField = new TextField(5);
        gbc.gridx = 1;
        paymentPanel.add(expiryField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        paymentPanel.add(new Label("CVV:"), gbc);

        TextField cvvField = new TextField(3);
        gbc.gridx = 1;
        paymentPanel.add(cvvField, gbc);

        Button confirmPaymentButton = new Button("Submit Payment");
        confirmPaymentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showReceiptPage();
            }
        });

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        paymentPanel.add(confirmPaymentButton, gbc);

        add(paymentPanel, BorderLayout.CENTER);

        validate();
        repaint();
    }

    private void showReceiptPage() {
        remove(paymentPanel);
        receiptPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        Label receiptTitle = new Label("Receipt", Label.CENTER);
        receiptTitle.setFont(new Font("Arial", Font.BOLD, 24));
        receiptTitle.setForeground(Color.magenta);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        receiptPanel.add(receiptTitle, gbc);

        gbc.gridy++;
        receiptPanel.add(new Label("Items Purchased:"), gbc);

        for (int i = 0; i < cartItems.size(); i++) {
            gbc.gridy++;
            receiptPanel.add(new Label(cartItems.get(i) + " x" + cartQuantities.get(i) + " - RM" + currency.format(cartPrices.get(i))), gbc);
        }

        gbc.gridy++;
        receiptPanel.add(new Label("Total: RM" + currency.format(totalAmount)), gbc);

        saveReceiptButton = new Button("Save Receipt");
        saveReceiptButton.addActionListener(this);

        loadReceiptButton = new Button("Load Receipt");
        loadReceiptButton.addActionListener(this);

        gbc.gridy++;
        gbc.gridx = 0;
        receiptPanel.add(saveReceiptButton, gbc);

        gbc.gridx = 1;
        receiptPanel.add(loadReceiptButton, gbc);

	gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        receiptPanel.add(historyButton, gbc);	

        add(receiptPanel, BorderLayout.CENTER);

        validate();
        repaint();
    }

    private void saveReceiptToFile() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter("receipt.txt", true));
            writer.println("Receipt:");
            for (int i = 0; i < cartItems.size(); i++) {
                writer.println(cartItems.get(i) + " x" + cartQuantities.get(i) + " - RM" + currency.format(cartPrices.get(i)));
            }
            writer.println("Total: RM" + currency.format(totalAmount));
            writer.println("-------------------------");
            System.out.println("Receipt saved.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }



   private void loadReceiptFromFile() {
    BufferedReader reader = null;
    try {
        reader = new BufferedReader(new FileReader("receipt.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

private void showHistoryPage() {
        remove(receiptPanel);
        historyPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        Label historyTitle = new Label("Receipt History", Label.CENTER);
        historyTitle.setFont(new Font("Arial", Font.BOLD, 24));
        historyTitle.setForeground(Color.magenta);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        historyPanel.add(historyTitle, gbc);

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("receipt.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                gbc.gridy++;
                historyPanel.add(new Label(line), gbc);
            }
        } catch (IOException e) {
            gbc.gridy++;
            historyPanel.add(new Label("No previous receipts found."), gbc);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        backButton = new Button("Back to Menu");
        backButton.addActionListener(this);

        gbc.gridy++;
        historyPanel.add(backButton, gbc);

        add(historyPanel, BorderLayout.CENTER);

        validate();
        repaint();
    }


    public void itemStateChanged(ItemEvent e) {
        recalculatePrice();
    }
}

