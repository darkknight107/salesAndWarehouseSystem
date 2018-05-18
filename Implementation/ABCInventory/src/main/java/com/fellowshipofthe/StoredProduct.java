package com.fellowshipofthe;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StoredProduct {
    private int productQuantity;

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }
}
