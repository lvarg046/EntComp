import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;


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

    public Storefront() throws FileNotFoundException {
        this.confirmItemButton.setEnabled(false);
        this.viewOrderButton.setEnabled(false);
        this.finishOrderButton.setEnabled(false);
        this.getInventoryFromFile();

        processItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inSTrue = new String("true");
                int itemsOrdered = Integer.parseInt(itemQuantityText.getText());
                int itemID = Integer.parseInt(itemIDText.getText());
                int itemIndex = idSearch(itemID);
                double itemDiscount = 0;
                NumberFormat nf = NumberFormat.getInstance();
                nf.setMaximumFractionDigits(2);

                if( itemsOrdered > 0 && itemsOrdered <= 4){
                    itemDiscount = 0.0;
                } else if ( itemsOrdered >= 5 && itemsOrdered <= 9){
                    itemDiscount = 0.10;
                } else if ( itemsOrdered >= 10 && itemsOrdered <= 14){
                    itemDiscount= 0.15;
                } else if (itemsOrdered >= 15 ){
                    itemDiscount = 0.20;
                }

                if(itemIndex != -1)
                {
                    Item foundItem = inventory.get(itemIndex);

                    String string1 = foundItem.getInStock();

//                    if( string1.compareTo(inSTrue) > 0){
                        String itemInfo = foundItem.getItemID() + foundItem.getItemDesc() + " $"+ nf.format(foundItem.getPrice()) + " " + itemsOrdered + " " + (itemDiscount*100)+"% $"+ nf.format((foundItem.getPrice() - (foundItem.getPrice() * itemDiscount)));
                        itemDetailsText.setText(itemInfo);
                        Storefront.this.confirmItemButton.setEnabled(true);
                        Storefront.this.processItemButton.setEnabled(false);
                        JOptionPane.showMessageDialog(null, "Item #1 Accepted. Added to your cart.");
//                    }


                } else {
                    JOptionPane.showMessageDialog( null, "Item ID "+ itemID + " not in file");
                }

                System.out.println("Item ID: "+ itemID);
                System.out.println("Items Ordered: "+itemsOrdered);
                System.out.println("itemInStock: "+ Storefront.this.inventory.get(itemIndex).getInStock());
            }
        });

        confirmItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
            String[] itemInfo = invItem.split(",");

            Item currentItem = new Item();
//            Boolean.parseBoolean.toString()
            currentItem.setItemID(Integer.parseInt(itemInfo[0]));
            currentItem.setItemDesc(itemInfo[1]);
            currentItem.setInStock(itemInfo[2]);
            currentItem.setPrice(Double.parseDouble(itemInfo[3]));
//            System.out.println( "ItemID: "+itemInfo[0]+" ItemDesc: "+ itemInfo[1]+" Item InStock: "+ itemInfo[2]+" ItemPrice: $"+ itemInfo[3]);
            inventory.add(currentItem);
//            System.out.println("End of while: "+ currentItem.getInStock());
        }
        textFile.close();

//        for (Item currentItem : inventory) {
//            System.out.println("ItemID: " + currentItem.getItemID() + ", Item Desc: " + currentItem.getItemDesc() + ", Item Price: $" + currentItem.getPrice() + ", In stock: " + currentItem.getInStock());
//        }
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
