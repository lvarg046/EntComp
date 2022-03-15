/*  Name: Luis Vargas
    Course: CNT 4713 - Spring 2022
    Assignment Title: Project 1 - Event-Driven Enterprise Simulation
    Due Date: Sunday, January 30, 2022
 */
//package NileDotCom;

public class Item {
    private double price;
    private int itemID;
    private String itemDesc;
    private String inStock;

    // Setters and Getters for Inventory items
    public double getPrice(){
        return price;
    }

    public void setPrice( double price ){
        this.price = price;
    }

    public int getItemID(){
        return itemID;
    }

    public void setItemID( int itemID ){
        this.itemID = itemID;
    }

    public String getItemDesc(){
        return itemDesc;
    }

    public void setItemDesc( String itemDesc ){
        this.itemDesc = itemDesc;
    }

    public String getInStock(){
        return inStock;
    }

    public void setInStock( boolean inStock ){
        this.inStock = String.valueOf(inStock);
    }

}
