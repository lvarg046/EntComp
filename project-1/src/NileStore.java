/*  Name: Luis Vargas
    Course: CNT 4713 - Spring 2022
    Assignment Title: Project 1 - Event-Driven Enterprise Simulation
    Due Date: Sunday, January 30, 2022
 */
import javax.swing.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class NileStore extends JFrame {
    private String inventoryFile = "inventory.txt";
    private ArrayList<Item> inventory;

    public void getInventoryFromFile() throws FileNotFoundException {
        this.inventory = new ArrayList<Item>();
        File file = new File(inventoryFile);
        Scanner textFile = new Scanner(file);

        while(textFile.hasNextLine() ){
            String item = textFile.nextLine();
            String[] itemInfo = item.split(",");

            Item currentItem = new Item();
            currentItem.setItemID(Integer.parseInt(itemInfo[0]));
            currentItem.setItemDesc(itemInfo[1]);
            currentItem.setInStock(Boolean.parseBoolean(itemInfo[2]));
            currentItem.setPrice(Double.parseDouble((itemInfo[3])));

            inventory.add(currentItem);
        }
        textFile.close();
    }
    public ArrayList<Item> getInventory() {
        return inventory;
    }
    public void setInventory( ArrayList<Item> inventory){
        this.inventory = inventory;
    }

    public static void main( String[] args ) throws FileNotFoundException {
        NileStore frame = new NileStore();
        frame.pack();
        frame.setTitle("Nile Dot Com - Spring 2022");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
