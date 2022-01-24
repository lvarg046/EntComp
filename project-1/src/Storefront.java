//package NileDotCom;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.*;
import java.util.*;
import java.awt.*;
import java.sql.*;


public class Storefront extends JFrame {
    private String inventoryFile = "C:\\Users\\Luis\\Desktop\\EntComp\\project-1\\src\\inventory.txt"; // Path to Inventory File
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
    public JButton confirmItemButton;
    private JButton viewOrderButton;
    private JButton finishOrderButton;
    private JButton newOrderButton;
    private JButton exitButton;
    private JButton processItemButton;
    private int lineItem = 1;
    private int totalItemsOrdered = 0;
    private double orderSize = 0;
    private Order order = new Order();
    private int counter = 1;


    public Storefront() throws FileNotFoundException {
        Storefront.this.processItemButton.setText("Process Item #"+ lineItem);
        Storefront.this.confirmItemButton.setText("Confirm Item #"+ lineItem);
        Storefront.this.orderSubtotalText.setText("$0.00");
        Storefront.this.confirmItemButton.setEnabled(false);
        Storefront.this.viewOrderButton.setEnabled(false);
        Storefront.this.finishOrderButton.setEnabled(false);
        Storefront.this.getInventoryFromFile();


        // Process Item
        processItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int itemsOrdered = Integer.parseInt(itemQuantityText.getText()); // Read from GUI
                int itemID = Integer.parseInt(itemIDText.getText()); // Read from GUI
                long itemIndex = idSearch(itemID); // itemID is in inventory

                if(itemIndex != -1) // ItemID is in Inventory
                {
                    Item foundItem = inventory.get((int) itemIndex); // Getting info from itemIndex
                    order.setItemInfo( foundItem.getItemID(), foundItem.getItemDesc(), String.format("%.2f",foundItem.getPrice())+"", itemsOrdered, (order.getDiscountPercent(itemsOrdered) * 100 ), String.format("%.2f", order.getFinalPrice(itemsOrdered, foundItem.getPrice()))+""); // Setting info for use in Order
                    String itemInfo = foundItem.getItemID()+" "+ foundItem.getItemDesc() + " $" + foundItem.getPrice() + " " + itemsOrdered + " " + order.getDiscountPercent(itemsOrdered)*100 +"% $"+String.format("%.3f", order.getFinalPrice(itemsOrdered, foundItem.getPrice()));
                    itemDetailsText.setText(itemInfo);
                    Storefront.this.confirmItemButton.setEnabled(true);
                    Storefront.this.processItemButton.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Item ID " + itemID + " not in file.");
                }

                // Debuggin'
                String[] iInfo = new String[6];
                iInfo = order.getItemInfo();
                System.out.println("Item ID: " + itemID);
                System.out.println("Items Ordered: " + itemsOrdered);
                System.out.println("itemInStock: " + Storefront.this.inventory.get((int) itemIndex).getInStock() );
                System.out.println("Price: "+Storefront.this.inventory.get((int) itemIndex).getPrice() );
                System.out.println("Discount Price: "+ iInfo[5]);
            }
        });

        // Confirm Item
        confirmItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inSTrue = "true";
                int comp;
                int itemID = Integer.parseInt(itemIDText.getText()); // Read from GUI
                int itemsOrdered = Integer.parseInt(itemQuantityText.getText()); // Read from GUI
                long index = idSearch(itemID); // Search for itemID in inventory

                if( index != -1 ){
                    // Item ID found in inventory
                    Item foundItem = inventory.get( (int) index); // Getting info from itemIndex after it's been found
                    String string1 = foundItem.getInStock(); // Used to determine stock status
                    comp = string1.compareTo(inSTrue);

                    if( comp == 0 ){
                        // ItemID is in stock
                        String ordList = Storefront.this.itemDetailsText.getText(); // String to send to Order class
                        order.setQuantity(itemsOrdered); // Set quantity of items ordered for using in
                        totalItemsOrdered += itemsOrdered; // Update total items ordered to send to Order class
                        order.setSubTotal(order.getFinalPrice(order.getQuantity(), Double.parseDouble(order.itemInfo[2]))); // Set subtotal for current order
                        order.addToOrderList((lineItem-1), ordList);
                        JOptionPane.showMessageDialog(null, "Item #" + lineItem + " accepted. Added to your cart."); // Alert confirming item was added to cart
                        lineItem++;
                        order.setTotalItems(totalItemsOrdered); // set total items ordered.

                        // Update GUI
                        Storefront.this.processItemButton.setText("Process Item #" + lineItem);
                        Storefront.this.confirmItemButton.setText("Confirm Item #" + lineItem);
                        Storefront.this.itemIDLabel.setText("Enter item ID for Item #" + lineItem + ":");
                        Storefront.this.itemQuantityLabel.setText("Enter Quantity for Item #" + lineItem + ":");
                        Storefront.this.itemDetailsLabel.setText("Details for Item #" + lineItem + ":");
                        Storefront.this.orderSubtotalText.setText("$"+String.format("%.2f", order.getSubTotal()));
                        Storefront.this.confirmItemButton.setEnabled(false);
                        Storefront.this.processItemButton.setEnabled(true);
                        Storefront.this.viewOrderButton.setEnabled(true);
                        Storefront.this.finishOrderButton.setEnabled(true);
                        Storefront.this.orderSubtotalLabel.setText("Order subtotal for "+order.getTotalItems()+" item(s):");
                    } else {
                        // Item ID is NOT in stock
                        JOptionPane.showMessageDialog(null, "Item ID "+ itemID +" is not in stock." );

                        // Update GUI
                        Storefront.this.processItemButton.setText("Process Item #" + lineItem);
                        Storefront.this.confirmItemButton.setText("Confirm Item #" + lineItem);
                        Storefront.this.itemIDLabel.setText("Enter item ID for Item #" + lineItem + ":");
                        Storefront.this.itemQuantityLabel.setText("Enter Quantity for Item #" + lineItem + ":");
                        Storefront.this.itemDetailsLabel.setText("Details for Item #" + lineItem + ":");
                        Storefront.this.orderSubtotalText.setText("$"+String.format("%.2f", order.getSubTotal()));
                        Storefront.this.confirmItemButton.setEnabled(false);
                        Storefront.this.processItemButton.setEnabled(true);
                        Storefront.this.viewOrderButton.setEnabled(true);
                    }
                } else {
                    // Item ID is not in inventory
                    JOptionPane.showMessageDialog(null, "Item ID " + itemID + " not in file.");
                }

            }
        });

        viewOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 // Counter for view order list
                StringBuilder viewOrder = new StringBuilder();

                for (String s : order.getOrderList()) { // Convert all to string and append onto viewOrder
                    viewOrder.append(counter).append(". ").append(s).append("\n");
                    counter++;
                }

                JOptionPane.showMessageDialog(null, viewOrder); // Display current order list
            }
        });

        finishOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder finishOrder = new StringBuilder();
                int local = 1;
                double taxRate = 0.06;
                double finalC, taxAmount;
                order.setFinalList( (counter - 1) ); // Making sure we get final order list
                order.setOrderTotalCost(taxRate, order.getSubTotal() );
                finalC = order.getOrderTotalCost();
                taxAmount = (taxRate * order.getSubTotal() );

                System.out.println("Subtotal: $"+order.getSubTotal()+"\nTax Rate: "+(taxRate*100)+"%"+"\nTax Amount: $"+String.format("%.3f", taxAmount)+"\nFinal Cost: $"+String.format("%.2f", finalC));




            }
        });

        newOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame[] frames = Frame.getFrames();
                for (Frame frame : frames) {
                    frame.dispose();
                }
                try {
                    Storefront.main(null);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }

    public int idSearch( int itemID){ // Linear search through inventory.txt file for corresponding itemID
        for(int i = 0 ; i < this.inventory.size(); i++){
            Item currentItem = inventory.get(i);
            if( currentItem.getItemID() == itemID )
                return i; // Found Item
        }
        return -1; // Didn't find Item
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public void setInventory( ArrayList<Item> inventory){
        this.inventory = inventory;
    }

    public void getInventoryFromFile() throws FileNotFoundException{
        this.inventory = new ArrayList<Item>();
        File file = new File(inventoryFile);
        Scanner textFile = new Scanner(file);

        while( textFile.hasNextLine()){
            String invItem = textFile.nextLine();
            String[] itemInfo = invItem.split(", ");

            Item currentItem = new Item();
//            Boolean.parseBoolean.toString()
            currentItem.setItemID(Integer.parseInt(itemInfo[0]));
            currentItem.setItemDesc(itemInfo[1]);
            currentItem.setInStock(Boolean.parseBoolean(itemInfo[2]));
            currentItem.setPrice(Double.parseDouble(String.format("%.2f", Double.parseDouble(itemInfo[3]))));
            inventory.add(currentItem);
        }
        textFile.close();
        // Debugging inventory list
//        for (Item currentItem : inventory) {
//            System.out.println("After File Close ItemID: " + currentItem.getItemID() + ", Item Desc: " + currentItem.getItemDesc() + ", Item Price: $" + currentItem.getPrice() + ", In stock: " + currentItem.getInStock());
//        }
    }

    
    private void invoiceFile( Timestamp ts){
        StringBuilder invoiceFile = new StringBuilder();
        SimpleDateFormat displayFormat = new SimpleDateFormat("M/dd/yyyy, h:mm:s aa z");
        displayFormat.setTimeZone(TimeZone.getDefault());
//        if( )
        File transactionsFile = new File("transactions.txt");
//        String invTransactionID = order.getTransactionID();

    }

    public static void main(String[] args) throws FileNotFoundException{
        JFrame frame = new JFrame();
        frame.setTitle("Nile Dot Com - Spring 2022");
        frame.setContentPane(new Storefront().panel1);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

}
