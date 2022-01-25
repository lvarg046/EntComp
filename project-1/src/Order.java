import java.text.*;
import java.util.*;
import java.sql.*;
import java.util.ArrayList;

public class Order {
    String[] itemInfo = new String[6];
    private double subTotal;
    private double discount;
    private int orderQuant;
    private int totalItems;
    private final ArrayList<String> orderList = new ArrayList<>();
    private String[] finalList;
    private String tID;
    private double orderTotalCost;
    public int counter = 1;
    private final StringBuilder allItemInfo = new StringBuilder();

    public double getOrderTotalCost(){
        return orderTotalCost;
    }
    public void setOrderTotalCost(double taxRate, double subTotal){
        this.orderTotalCost = ( (taxRate*subTotal) + subTotal );
    }
    public String[] getFinalList(){
        return finalList;
    }
    public void setFinalList( int size ){ // Determines, and formats the final list of items ordered
        finalList = new String[size];
        int i = 0;
        int count = 1;
        for (String s : orderList) {
            finalList[i++] = String.valueOf(count++).concat(". ").concat(s);
        }
    }
    public String getTransactionID(){
        return tID;
    }
    public void setTransactionID(Timestamp ts){ // Generates unique transaction ID for the transaction.txt file
        StringBuilder transactionID = new StringBuilder();
        SimpleDateFormat form = new SimpleDateFormat("ddMMyyyyHHmm");
        form.setTimeZone(TimeZone.getDefault());
        transactionID.append(form.format(ts));
        tID = transactionID.toString();
    }
    public ArrayList<String> getOrderList() {
         return orderList;
    }
    public void addToOrderList( int index, String infoText ){
        orderList.add(index, infoText);
    }
    public int getTotalItems(){ return totalItems; }
    public void setTotalItems( int itemsOrdered ){
        this.totalItems = itemsOrdered;
    }
    public int getQuantity() { return orderQuant; }
    public void setQuantity ( int orderQuant ){
        this.orderQuant = orderQuant;
    }
    public double getDiscountPercent(int itemsOrdered){ // Determines discount percentage
        this.setTotalItems(itemsOrdered);
        if( itemsOrdered > 0 && itemsOrdered <= 4){
            discount = 0.0;
        } else if ( itemsOrdered >= 5 && itemsOrdered <= 9){
            discount = 0.10;
        } else if ( itemsOrdered >= 10 && itemsOrdered <= 14){
            discount = 0.15;
        } else if (itemsOrdered >= 15 ){
            discount = 0.20;
        }
        return discount;
    }
    public double getSubTotal(){ return subTotal; }
    public void setSubTotal( double itemPrice ){
        this.subTotal = this.subTotal +  itemPrice*( this.getQuantity() );
    }
    public double getFinalPrice (int itemsOrdered, double initPrice) { // Determines Final Price per item with discount
        double disc = getDiscountPercent(itemsOrdered);
        double finalPrice = initPrice;
        if( disc == 0.0 ) { return initPrice;}
        else if( disc == 0.10 ){
            finalPrice = initPrice - (initPrice * disc ); // 10%
        } else if( disc == 0.15 ){
            finalPrice = initPrice - ( initPrice * disc ); // 15%
        } else if( disc == 0.20 ){
            finalPrice = initPrice - ( initPrice * disc ); // 20%
        }
        return finalPrice;
    }
    public StringBuilder getAllItemInfo(){ return allItemInfo; } // Contains all info to be printed in transactions.txt
    public void setAllItemInfo(Timestamp ts){ // Creates info to write to transaction.txt file
        StringBuilder invoiceFile = new StringBuilder();
        SimpleDateFormat format = new SimpleDateFormat("M/dd/yyyy, h:mm:ss aa z");
        format.setTimeZone(TimeZone.getDefault());
        invoiceFile
                .append(this.getTransactionID()).append(", ")  // Transaction ID
                .append(this.itemInfo[0]).append(", ")         // Item ID
                .append(this.itemInfo[1]).append(", ")         // Item Description
                .append(this.itemInfo[2]).append(", ")         // Item Price without a discount
                .append(this.itemInfo[3]).append(", ")         // Quantity of items ordered
                .append(this.itemInfo[4]).append(", ")         // Discount percentage
                .append("$").append(this.itemInfo[5])          // Price per Item with discount applied
                .append(", ").append(format.format(ts)).append("\n"); // Date & Time
        allItemInfo.append(invoiceFile); // Adds all above info to the object for later use
    }
    public void setItemInfo( // adds Item Info to an organized array.
            int itemID, String itemDesc,
            String initialPrice, int itemsOrdered,
            double totalDiscount, String finalPrice )
    {
        itemInfo[0] = String.valueOf(itemID);
        itemInfo[1] = itemDesc;
        itemInfo[2] = initialPrice;
        itemInfo[3] = String.valueOf(itemsOrdered);
        itemInfo[4] = String.valueOf(totalDiscount);
        itemInfo[5] = finalPrice;
    }
}