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
    private double orderSTotal = 0;
    private double orderSize = 0;
    private Order order = new Order();


    public Storefront() throws FileNotFoundException {
        Storefront.this.processItemButton.setText("Process Item #"+ lineItem);
        Storefront.this.confirmItemButton.setText("Confirm Item #"+ lineItem);
        Storefront.this.confirmItemButton.setEnabled(false);
        Storefront.this.viewOrderButton.setEnabled(false);
        Storefront.this.finishOrderButton.setEnabled(false);
        Storefront.this.getInventoryFromFile();


        // Process Item
        processItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int itemsOrdered = Integer.parseInt(itemQuantityText.getText());
                int itemID = Integer.parseInt(itemIDText.getText());
                long itemIndex = idSearch(itemID); // making sure that itemID is the inventory.txt file

                if(itemIndex != -1)
                {
                    Item foundItem = inventory.get((int) itemIndex); // Getting info from itemIndex after it's been found
                    order.setItemInfo( foundItem.getItemID(), foundItem.getItemDesc(), String.format("%.2f",foundItem.getPrice())+"", itemsOrdered, (order.getDiscountPercent(itemsOrdered) * 100 ), String.format("%.2f", order.getFinalPrice(itemsOrdered, foundItem.getPrice()))+"");
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
                int comp;
                String inSTrue = "true";
                int itemID = Integer.parseInt(itemIDText.getText());
                int itemsOrdered = Integer.parseInt(itemQuantityText.getText());

                long index = idSearch(itemID);
                if( index != -1 ){
                    Item foundItem = inventory.get( (int) index); // Getting info from itemIndex after it's been found
                    // Used to determine stock status
                    String string1 = foundItem.getInStock();
                    comp = string1.compareTo(inSTrue);

                    if( comp == 0 ){
                        Storefront.this.itemDetailsText.getText();
                        order.setQuantity(itemsOrdered);
                        order.setSubTotal(order.getFinalPrice(order.getQuantity(), Double.parseDouble(order.itemInfo[2])));
                        JOptionPane.showMessageDialog(null, "Item #" + lineItem + " accepted. Added to your cart.");

                        lineItem++;
                        Storefront.this.processItemButton.setText("Process Item #" + lineItem);
                        Storefront.this.confirmItemButton.setText("Confirm Item #" + lineItem);
                        Storefront.this.itemIDLabel.setText("Enter item ID for Item #" + lineItem + ":");
                        Storefront.this.itemQuantityLabel.setText("Enter Quantity for Item #" + lineItem + ":");
                        Storefront.this.itemDetailsLabel.setText("Details for Item #" + lineItem + ":");
                        Storefront.this.orderSubtotalText.setText("$"+String.format("%.2f", order.getSubTotal()));
                        Storefront.this.confirmItemButton.setEnabled(false);
                        Storefront.this.processItemButton.setEnabled(true);
                        Storefront.this.viewOrderButton.setEnabled(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Item ID "+ itemID +" is not in stock." );
                        Storefront.this.processItemButton.setText("Process Item #" + lineItem);
                        Storefront.this.confirmItemButton.setText("Confirm Item #" + lineItem);
                        Storefront.this.itemIDLabel.setText("Enter item ID for Item #" + lineItem + ":");
                        Storefront.this.itemQuantityLabel.setText("Enter Quantity for Item #" + lineItem + ":");
                        Storefront.this.itemDetailsLabel.setText("Details for Item #" + lineItem + ":");
                        Storefront.this.confirmItemButton.setEnabled(false);
                        Storefront.this.processItemButton.setEnabled(true);
                        Storefront.this.viewOrderButton.setEnabled(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Item ID " + itemID + " not in file.");
                }

            }
        });

        viewOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        finishOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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

    public int idSearch( int itemID){
        for(int i = 0 ; i < this.inventory.size(); i++){
            Item currentItem = inventory.get(i);
            if( currentItem.getItemID() == itemID )
                return i;
        }
        return -1;
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
//            System.out.println( "ItemID: "+itemInfo[0]+" ItemDesc: "+ itemInfo[1]+" Item InStock: "+itemInfo[2]);
            inventory.add(currentItem);
//            System.out.println("End of while: "+ currentItem.getInStock());
        }
        textFile.close();
        // For Debugging
//        for (Item currentItem : inventory) {
//            System.out.println("After File Close ItemID: " + currentItem.getItemID() + ", Item Desc: " + currentItem.getItemDesc() + ", Item Price: $" + currentItem.getPrice() + ", In stock: " + currentItem.getInStock());
//        }
    }


    public JTextField getOrderSubtotalText() {
        return orderSubtotalText;
    }

    public void setOrderSubtotalText(JTextField orderSubtotalText) {
        this.orderSubtotalText = orderSubtotalText;
    }
    
    private void invoiceFile( Timestamp ts){
        StringBuilder invoiceFile = new StringBuilder();
        SimpleDateFormat transactionID = new SimpleDateFormat("ddMMyyyyHHmm");
        SimpleDateFormat displayFormat = new SimpleDateFormat("M/dd/yyyy, h:mm:s aa z");
        displayFormat.setTimeZone(TimeZone.getDefault());
//        if( )
        File transactionsFile = new File("transactions.txt");
        String invTransactionID = transactionID.format(ts);

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
