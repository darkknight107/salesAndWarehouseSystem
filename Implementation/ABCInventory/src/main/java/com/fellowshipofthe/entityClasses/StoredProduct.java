package com.fellowshipofthe.entityClasses;

public class StoredProduct {
    private String productItemCode;
    private String productQuantity;
    private String locationID;

    public StoredProduct(){
        this.productItemCode="";
        this.locationID="";
        this.productQuantity="";
    }


   public String getProductItemCode(){
        return this.productItemCode;
   }

    public void setProductItemCode(String productItemCode) {
        this.productItemCode = productItemCode;
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
