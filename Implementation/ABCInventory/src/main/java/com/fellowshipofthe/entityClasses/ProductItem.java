package com.fellowshipofthe.entityClasses;

public class ProductItem {
    private String productItemCode;
    private String productSize;
    private String productCode;


    public ProductItem(){
        this.productItemCode="";
        this.productCode="";
        this.productSize="";
    }
    public String getProductItemCode() {
        return productItemCode;
    }

    public void setProductItemCode(String productItemCode) {
        this.productItemCode = productItemCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductCode(){
        return this.productCode;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }
}
