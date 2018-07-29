package entityClass;

public class StoredProduct {
    private String productItemCode;
    private String locationID;
    private String productQuantity;

    public StoredProduct(String productItemCode, String locationID, String productQuantity) {
        this.productItemCode = productItemCode;
        this.locationID = locationID;
        this.productQuantity = productQuantity;
    }

    public StoredProduct() {
    }

    public String getProductItemCode() {
        return productItemCode;
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
