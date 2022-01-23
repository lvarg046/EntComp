public class Order {
    String[] itemInfo = new String[6];
    private double subTotal;
    private double discount;
    private int orderQuant;
    private int totalItems;

    public int getQuantity() { return orderQuant; }

    public void setQuantity ( int orderQuant ){
        this.orderQuant = orderQuant;
    }

    public double getDiscountPercent(int itemsOrdered){
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

    public double getFinalPrice (int itemsOrdered, double initPrice) { // Final Price per item
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

    public String[] getItemInfo(){
        return itemInfo;
    }
    public void setItemInfo(int itemID, String itemDesc, String initialPrice, int itemsOrdered, double totalDiscount, String finalPrice ){
        itemInfo[0] = String.valueOf(itemID);
        itemInfo[1] = itemDesc;
        itemInfo[2] = initialPrice;
        itemInfo[3] = String.valueOf(itemsOrdered);
        itemInfo[4] = String.valueOf(totalDiscount);
        itemInfo[5] = finalPrice;
    }
}
