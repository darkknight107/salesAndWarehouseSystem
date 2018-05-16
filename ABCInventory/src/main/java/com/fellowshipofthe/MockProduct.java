package com.fellowshipofthe;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MockProduct {
    private String productName;
    private int productCode;
    private double price;
    private String description;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductCode() {
        return productCode;
    }

    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
