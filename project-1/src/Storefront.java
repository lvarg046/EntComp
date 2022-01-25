/*  Name: Luis Vargas
    Course: CNT 4713 - Spring 2022
    Assignment Title: Project 1 - Event-Driven Enterprise Simulation
    Due Date: Sunday, January 30, 2022
 */
//package NileDotCom;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.text.*;
import java.util.*;
import java.awt.*;
import java.sql.*;

public class Storefront extends JFrame {
    private static final Order order = new Order();
    private static final String process = "Process Item #";
    private static final String confirm = "Confirm Item #";
    private static final String IDText = "Enter Item ID for Item #";
    private static final String iQuantity = "Enter Quantity for Item #";
    private static final String iDetails = "Details for Item #";
    private static final Path inventoryFile = Paths.get("src/inventory.txt"); // Path to Inventory File
    private static final String transactionFilePath = "transactions.txt";
    private ArrayList<Item> inventory;
    private JPanel panel1;
    private JPanel panel2;
    private JTextField itemIDText;
    private JTextField itemDetailsText;
    private JTextField itemQuantityText;
    private JTextField orderSubtotalText;
    private JLabel itemIDLabel;
    private JLabel itemQuantityLabel;
    private JLabel itemDetailsLabel;
    private JLabel orderSubtotalLabel;
    private JButton confirmItemButton;
    private JButton viewOrderButton;
    private JButton finishOrderButton;
    private JButton newOrderButton;
    private JButton exitButton;
    private JButton processItemButton;
    private int lineItem = 1;
    private int totalItemsOrdered = 0;

    public Storefront() throws FileNotFoundException {

        // Initialize GUI State
        Storefront.this.processItemButton.setText(process + lineItem);
        Storefront.this.confirmItemButton.setText(confirm + lineItem);
        Storefront.this.orderSubtotalText.setText("$0.00");
        Storefront.this.confirmItemButton.setEnabled(false);
        Storefront.this.viewOrderButton.setEnabled(false);
        Storefront.this.finishOrderButton.setEnabled(false);
        Storefront.this.getInventoryFromFile();
        Timestamp ts = new Timestamp(new Date().getTime());

        // Process Item
        processItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Variables
                String inSTrue = "true"; // Comparator
                int comp; // Comparator
                int itemsOrdered = Integer.parseInt(itemQuantityText.getText()); // Read from GUI
                int itemID = Integer.parseInt(itemIDText.getText()); // Read from GUI
                long itemIndex = idSearch(itemID); // itemID is in inventory
                if (itemIndex != -1) // ItemID is in Inventory
                {
                    Item foundItem = inventory.get((int) itemIndex); // Getting info from itemIndex
                    String string1 = foundItem.getInStock(); // Used to determine stock status
                    comp = string1.compareTo(inSTrue);
                    if (comp == 0) {// Item ID is in Stock
                        String itemInfo = foundItem.getItemID() + " " + foundItem.getItemDesc() + " $" + foundItem.getPrice() + " " + itemsOrdered + " " + order.getDiscountPercent(itemsOrdered) * 100 + "% $" + String.format("%.3f", order.getFinalPrice(itemsOrdered, foundItem.getPrice()));
                        itemDetailsText.setText(itemInfo);
                        Storefront.this.confirmItemButton.setEnabled(true);
                        Storefront.this.processItemButton.setEnabled(false);
                        Storefront.this.itemIDText.setEditable(false);
                    } else {// Item ID is NOT in stock
                        // Error Message generator
                        JOptionPane.showMessageDialog(panel1, "Item ID " + itemID + " is not in stock.", "NILE DOT COM - ERROR", JOptionPane.ERROR_MESSAGE);
                        // Update GUI
                        Storefront.this.itemDetailsText.setText("");
                        Storefront.this.itemIDText.setText("");
                        Storefront.this.itemQuantityText.setText("");
                        Storefront.this.processItemButton.setText(process + lineItem);
                        Storefront.this.confirmItemButton.setText(confirm + lineItem);
                        Storefront.this.itemIDLabel.setText(IDText + lineItem + ":");
                        Storefront.this.itemQuantityLabel.setText(iQuantity + lineItem + ":");
                        Storefront.this.itemDetailsLabel.setText(iDetails + lineItem + ":");
                        Storefront.this.confirmItemButton.setEnabled(false);
                        Storefront.this.processItemButton.setEnabled(true);
                        Storefront.this.viewOrderButton.setEnabled(true);
                    }
                } else {// Item ID is NOT in inventory
                    JOptionPane.showMessageDialog(null, "Item ID " + itemID + " not in file.", "NILE DOT COM - ERROR", JOptionPane.ERROR_MESSAGE);
                    Storefront.this.itemDetailsText.setText("");
                    Storefront.this.processItemButton.setText(process + lineItem);
                    Storefront.this.confirmItemButton.setText(confirm + lineItem);
                    Storefront.this.itemIDLabel.setText(IDText + lineItem + ":");
                    Storefront.this.itemQuantityLabel.setText(iQuantity + lineItem + ":");
                    Storefront.this.itemDetailsLabel.setText(iDetails + lineItem + ":");
                    Storefront.this.confirmItemButton.setEnabled(false);
                    Storefront.this.processItemButton.setEnabled(true);
                    Storefront.this.viewOrderButton.setEnabled(true);
                }
            }
        });

        // Confirm Item
        confirmItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Variables
                int itemID = Integer.parseInt(itemIDText.getText()); // Read from GUI
                int itemsOrdered = Integer.parseInt(itemQuantityText.getText()); // Read from GUI
                long index = idSearch(itemID); // Search for itemID in inventory
                if (index != -1) {
                    // Item ID found in inventory
                    Item foundItem = inventory.get((int) index); // Getting info from itemIndex
                    order.setItemInfo( // Setting item info for confirmed item
                            foundItem.getItemID(),
                            foundItem.getItemDesc(),
                            String.format("%.2f", foundItem.getPrice()) + "",
                            itemsOrdered,
                            (order.getDiscountPercent(itemsOrdered) * 100),
                            String.format("%.2f", order.getFinalPrice(itemsOrdered, foundItem.getPrice())) + ""); // Setting info for use in Order
                    order.setTransactionID(ts); // Generating transaction ID for each Item
                    String ordList = Storefront.this.itemDetailsText.getText(); // String to send to Order class
                    order.setQuantity(itemsOrdered); // Set quantity of items ordered for using in
                    totalItemsOrdered += itemsOrdered; // Update total items ordered to send to Order class
                    order.setSubTotal(// Set subtotal for current order
                            order.getFinalPrice(order.getQuantity(), Double.parseDouble(order.itemInfo[2])));
                    order.addToOrderList((lineItem - 1), ordList); // Adding to running list of ordered items
                    // Alert confirming item was added to cart
                    JOptionPane.showMessageDialog(
                            panel1,
                            "Item #" + lineItem + " accepted. Added to your cart.",
                            "NILE DOT COM - Item Confirmed",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    lineItem++; // keeping track of line items
                    order.setTotalItems(totalItemsOrdered); // set total items ordered.
                    order.setAllItemInfo(ts); // Set ALL item info with current timestamp
                    // Update GUI items
                    Storefront.this.itemIDText.setText(""); // Clear for next input
                    Storefront.this.itemQuantityText.setText(""); // Clear for next input
                    Storefront.this.itemDetailsText.setText("");
                    Storefront.this.processItemButton.setText(process + lineItem);
                    Storefront.this.confirmItemButton.setText(confirm + lineItem);
                    Storefront.this.itemIDLabel.setText(IDText + lineItem + ":");
                    Storefront.this.itemQuantityLabel.setText(iQuantity + lineItem + ":");
                    Storefront.this.itemDetailsLabel.setText(iDetails + lineItem + ":");
                    Storefront.this.orderSubtotalText.setText("$" + String.format("%.2f", order.getSubTotal()));
                    Storefront.this.orderSubtotalLabel.setText("Order subtotal for " + order.getTotalItems() + " item(s):");
                    Storefront.this.confirmItemButton.setEnabled(false);
                    Storefront.this.processItemButton.setEnabled(true);
                    Storefront.this.viewOrderButton.setEnabled(true);
                    Storefront.this.finishOrderButton.setEnabled(true);
                    Storefront.this.itemIDText.setEditable(true);
                }
            }
        });

        viewOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Counter for view order list
                StringBuilder viewOrder = new StringBuilder();
                SimpleDateFormat format = new SimpleDateFormat("M/dd/yyyy, h:mm:ss aa z");
                format.setTimeZone(TimeZone.getDefault());
                double taxRate = 0.06;
                double finalC;
                double taxAmount;
                int local;
                order.counter = 1;
                for (String s : order.getOrderList()) { // Convert all to string and append onto viewOrder
                    viewOrder.append(order.counter).append(". ").append(s).append("\n");
                    order.counter++;
                }
                order.setFinalList((order.counter - 1));
                order.setOrderTotalCost(taxRate, order.getSubTotal()); // Total Cost calculation
                finalC = order.getOrderTotalCost(); // Getting final order costs
                taxAmount = (taxRate * order.getSubTotal()); // Calculating the tax amount based on the subtotal and tax rate

                // Building the dialog output
                local = lineItem - 1;
                if (!Storefront.this.finishOrderButton.isEnabled() && !Storefront.this.processItemButton.isEnabled()) {
                    viewOrder.setLength(0);
                    viewOrder.append("Date: ").append(format.format(ts)).append("\n\n"); // using formatted date & time
                    viewOrder.append("Number of line items: ").append(local).append("\n\n"); // making all the line items
                    viewOrder.append("Item # / ID / Title / Price / Qty / Disc % / Subtotal:").append("\n\n");
                    viewOrder.append(String.join("\n", order.getFinalList())).append("\n\n");
                    viewOrder.append("Order Subtotal: \t$").append(String.format("%.2f", order.getSubTotal())).append("\n\n");
                    viewOrder.append("Tax rate: \t").append(taxRate * 100).append("%").append("\n\n");
                    viewOrder.append("Tax Amount: \t$").append(String.format("%.2f", taxAmount)).append("\n\n");
                    viewOrder.append("Order Total: \t$").append(String.format("%.2f", finalC)).append("\n\n");
                    viewOrder.append("Thanks for shopping at Nile Dot Com!\n");

                    // Final Invoice generator for viewing again after submission
                    JOptionPane.showMessageDialog(
                            panel1,
                            viewOrder,
                            "NILE DOT COM - FINAL INVOICE",
                            JOptionPane.INFORMATION_MESSAGE
                    ); // Display current order list
                } else {
                    viewOrder.append("\n\n").append("Order Subtotal: $")
                            .append(String.format("%.2f", order.getSubTotal())).append("\n\n");

                    // View Order Generator
                    JOptionPane.showMessageDialog(
                            panel1,
                            viewOrder,
                            "NILE DOT COM - CURRENT SHOPPING CART STATUS",
                            JOptionPane.INFORMATION_MESSAGE
                    ); // Display current order list
                }
            }
        });

        finishOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder finishOrder = new StringBuilder();
                SimpleDateFormat format = new SimpleDateFormat("M/dd/yyyy, h:mm:ss aa z");
                format.setTimeZone(TimeZone.getDefault());
                double taxRate = 0.06;
                double finalC;
                double taxAmount;
                int local;
                order.counter = 1;

                for (String s : order.getOrderList()) {
                    order.counter++;
                }// count all the items in the order list
                order.setFinalList((order.counter - 1)); // Making sure we get final order list
                order.setOrderTotalCost(taxRate, order.getSubTotal()); // Total Cost calculation
                finalC = order.getOrderTotalCost(); // Getting final order costs
                taxAmount = (taxRate * order.getSubTotal()); // Calculating the tax amount based on the subtotal and tax rate

                // Building the dialog output
                local = lineItem - 1;
                finishOrder
                        .append("Date: ").append(format.format(ts)).append("\n\n")// using formatted date & time
                        .append("Number of line items: ").append(local).append("\n\n") // making all the line items
                        .append("Item# / ID / Title / Price / Qty / Disc % / Subtotal:").append("\n\n")
                        .append(String.join("\n", order.getFinalList())).append("\n\n")
                        .append("Order Subtotal: \t$").append(String.format("%.2f", order.getSubTotal())).append("\n\n")
                        .append("Tax rate: \t").append(taxRate * 100).append("%").append("\n\n")
                        .append("Tax Amount: \t$").append(String.format("%.2f", taxAmount)).append("\n\n")
                        .append("Order Total: \t$").append(String.format("%.2f", finalC)).append("\n\n")
                        .append("Thanks for shopping at Nile Dot Com!\n");

                // Create transaction file
                transactionFile(order.getAllItemInfo());

                // Display Finalized Item Order list
                JOptionPane.showMessageDialog(
                        panel1,
                        finishOrder,
                        "NILE DOT COM - FINAL INVOICE",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // Update GUI
                Storefront.this.itemIDText.setEditable(false);
                Storefront.this.itemDetailsText.setEditable(false);
                Storefront.this.itemQuantityText.setEditable(false);
                Storefront.this.processItemButton.setEnabled(false);
                Storefront.this.confirmItemButton.setEnabled(false);
                Storefront.this.finishOrderButton.setEnabled(false);
                Storefront.this.viewOrderButton.setEnabled(true);
                Storefront.this.newOrderButton.setEnabled(true);
            }
        });

        // New Order
        newOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame[] frames = Frame.getFrames();
                for (Frame frame : frames) { // Loop through all frames and dispose of them
                    frame.dispose();
                }
                try {
                    Storefront.main(null); // Create a new order
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Exit
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the program
            }
        });

    }

    public int idSearch(int itemID) { // Linear search through inventory.txt file for corresponding itemID
        for (int i = 0; i < this.inventory.size(); i++) {
            Item currentItem = inventory.get(i);
            if (String.valueOf(currentItem.getItemID()).equals(String.valueOf(itemID)))
                return i; // Found Item ID
        }
        return -1; // Didn't find Item ID
    }

    public void getInventoryFromFile() throws FileNotFoundException {
        this.inventory = new ArrayList<Item>();
        File file = new File(String.valueOf(inventoryFile));
        Scanner textFile = new Scanner(file);

        // Reading from Inventory.txt file and adding info to Inventory Item List
        while (textFile.hasNextLine()) {
            String invItem = textFile.nextLine();
            String[] itemInfo = invItem.split(", ");
            Item currentItem = new Item();
            currentItem.setItemID(Integer.parseInt(itemInfo[0]));
            currentItem.setItemDesc(itemInfo[1]);
            currentItem.setInStock(Boolean.parseBoolean(itemInfo[2]));
            currentItem.setPrice(Double.parseDouble(String.format("%.2f", Double.parseDouble(itemInfo[3]))));
            inventory.add(currentItem);
        }
        textFile.close();
    }

    public void transactionFile(StringBuilder invoiceFile) { // Writes transaction to corresponding text file
        File transactionFile = new File(transactionFilePath);
        if (transactionFile.exists() && !transactionFile.isDirectory()) {
            try {
                Files.write(Paths.get(transactionFilePath), invoiceFile.toString().getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            PrintWriter writer;
            try {
                writer = new PrintWriter(transactionFilePath, StandardCharsets.UTF_8);
                writer.print(invoiceFile);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        JFrame frame = new JFrame();
        frame.setTitle("Nile Dot Com - Spring 2022");
        frame.setContentPane(new Storefront().panel1);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        itemIDText = new JTextField();
        itemIDText.setText("");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel1.add(itemIDText, gbc);
        itemDetailsText = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel1.add(itemDetailsText, gbc);
        itemQuantityText = new JTextField();
        itemQuantityText.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 3.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel1.add(itemQuantityText, gbc);
        itemIDLabel = new JLabel();
        itemIDLabel.setText("Enter Item ID for Item #1:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 0, 10);
        panel1.add(itemIDLabel, gbc);
        itemDetailsLabel = new JLabel();
        itemDetailsLabel.setText("Details for Item #1: ");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 0, 10);
        panel1.add(itemDetailsLabel, gbc);
        orderSubtotalLabel = new JLabel();
        orderSubtotalLabel.setText("Order Subtotal for 0 item(s):");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 0, 10);
        panel1.add(orderSubtotalLabel, gbc);
        panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel2, gbc);
        processItemButton = new JButton();
        processItemButton.setEnabled(true);
        processItemButton.setText("Process Item #");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel2.add(processItemButton, gbc);
        viewOrderButton = new JButton();
        viewOrderButton.setText("View Order");
        viewOrderButton.setToolTipText("View the order so far.");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel2.add(viewOrderButton, gbc);
        finishOrderButton = new JButton();
        finishOrderButton.setText("Finish Order");
        finishOrderButton.setToolTipText("Completes an order and generatees an invoice and adds to the transaction file");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel2.add(finishOrderButton, gbc);
        newOrderButton = new JButton();
        newOrderButton.setText("New Order");
        newOrderButton.setToolTipText("Clears order and starts over.");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel2.add(newOrderButton, gbc);
        exitButton = new JButton();
        exitButton.setText("Exit");
        exitButton.setToolTipText("Quit");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel2.add(exitButton, gbc);
        confirmItemButton = new JButton();
        confirmItemButton.setText("Confirm Item #");
        confirmItemButton.setToolTipText("Confirms that the item is correct.");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel2.add(confirmItemButton, gbc);
        orderSubtotalText = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel1.add(orderSubtotalText, gbc);
        itemQuantityLabel = new JLabel();
        itemQuantityLabel.setText("Enter Quantity for Item #1:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 0, 10);
        panel1.add(itemQuantityLabel, gbc);
        itemIDLabel.setLabelFor(itemIDText);
        itemDetailsLabel.setLabelFor(itemDetailsText);
        orderSubtotalLabel.setLabelFor(orderSubtotalText);
        itemQuantityLabel.setLabelFor(itemQuantityText);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
