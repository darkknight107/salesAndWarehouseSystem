package com.fellowshipofthe.entityClasses;

public class StoredProduct extends ProductItem {
    private String productItemCode;
    private String productQuantity;
    private String locationID;

    public StoredProduct(){
        this.productItemCode="";
        this.locationID="";
        this.productQuantity="";
    }

   @Override

   public String getProductItemCode(){
        return this.productItemCode;
   }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }
    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

}
