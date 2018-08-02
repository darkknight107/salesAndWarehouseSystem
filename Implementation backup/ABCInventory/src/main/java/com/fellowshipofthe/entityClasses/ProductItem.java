package com.fellowshipofthe.entityClasses;

public class ProductItem extends Product{
    protected String productItemCode;
    private String productSize;


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

    @Override
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
