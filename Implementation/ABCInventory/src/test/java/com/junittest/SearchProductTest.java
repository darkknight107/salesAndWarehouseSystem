package com.junittest;

import com.fellowshipofthe.SearchProduct;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static junit.framework.TestCase.assertEquals;

public class SearchProductTest {
    //initializing SearchProduct object and fields
    SearchProduct searchProduct= null;
    Field productItemField= null;
    Field productSizeField= null;
    Field productCodeField= null;
    Field productNameField=null;
    Field productQuantityField= null;
    Field priceField= null;
    Field locationIDField= null;
    Field locationNameField= null;
    Field locationAddressField= null;
    Field phoneField= null;

    @Before
    public void setUp() throws Exception {
        //creating a new SearchProduct object
        searchProduct= new SearchProduct();
    }
    //test productItemCode setter and getter
    @Test
    public void testSetProductItemCode() throws NoSuchFieldException, IllegalAccessException {
        //when
        searchProduct.setProductItemCode("A2100");
        //then
        productItemField= searchProduct.getClass().getDeclaredField("productItemCode");
        productItemField.setAccessible(true);
        assertEquals("Testing setProductItemCode", productItemField.get(searchProduct), "A2100");
    }
    @Test
    public void testGetProductItemCode() throws NoSuchFieldException, IllegalAccessException {
        //given
        productItemField = searchProduct.getClass().getDeclaredField("productItemCode");
        productItemField.setAccessible(true);
        productItemField.set(searchProduct, "A1100");
        //when
        final String result= searchProduct.getProductItemCode();
        //then
        assertEquals("testing getProductItemCode", result, "A1100");
    }
    //test productSize setter and getter
    @Test
    public void testSetProductSize() throws NoSuchFieldException, IllegalAccessException {
        //when
        searchProduct.setProductSize("M");
        //then
        productSizeField= searchProduct.getClass().getDeclaredField("productSize");
        productSizeField.setAccessible(true);
        assertEquals("Testing setProductSize", productSizeField.get(searchProduct), "M");
    }
    @Test
    public void testGetProductSize() throws NoSuchFieldException, IllegalAccessException {
        //given
        productSizeField= searchProduct.getClass().getDeclaredField("productSize");
        productSizeField.setAccessible(true);
        productSizeField.set(searchProduct, "XS");
        //when
        final String result= searchProduct.getProductSize();
        //then
        assertEquals("testing getProductSize", result, "XS");
    }
    //test productCode setter and getter
    @Test
    public void testSetProductCode() throws NoSuchFieldException, IllegalAccessException {
        //when
        searchProduct.setProductCode("C1");
        //then
        productCodeField= searchProduct.getClass().getDeclaredField("productCode");
        productCodeField.setAccessible(true);
        assertEquals("Testing setProductCode", productCodeField.get(searchProduct), "C1");
    }
    @Test
    public void testGetProductCode() throws NoSuchFieldException, IllegalAccessException {
        //given
        productCodeField= searchProduct.getClass().getDeclaredField("productCode");
        productCodeField.setAccessible(true);
        productCodeField.set(searchProduct,"C2");
        //when
        final String result= searchProduct.getProductCode();
        //then
        assertEquals("Testing getProductCode", result, "C2");
    }
    //test productName setter and getter
    @Test
    public void testSetProductName() throws NoSuchFieldException, IllegalAccessException {
        //when
        searchProduct.setProductName("Jackson Tshirt");
        //then
        productNameField= searchProduct.getClass().getDeclaredField("productName");
        productNameField.setAccessible(true);
        assertEquals("testing setProductName", productNameField.get(searchProduct), "Jackson Tshirt");
    }
    @Test
    public void testGetProductName() throws NoSuchFieldException, IllegalAccessException {
        //given
        productNameField= searchProduct.getClass().getDeclaredField("productName");
        productNameField.setAccessible(true);
        productNameField.set(searchProduct,"Jersey Tshirt");
        //when
        final String result= searchProduct.getProductName();
        //then
        assertEquals("testing getProductName", result, "Jersey Tshirt");
    }
    //test productQuantity setter and getter
    @Test
    public void testSetProductQuantity() throws NoSuchFieldException, IllegalAccessException {
        //when
        searchProduct.setProductQuantity(10);
        //then
        productQuantityField= searchProduct.getClass().getDeclaredField("productQuantity");
        productQuantityField.setAccessible(true);
        assertEquals("test setProductQuantity", productQuantityField.get(searchProduct), 10);
    }
    @Test
    public void testGetProductQuantity() throws NoSuchFieldException, IllegalAccessException {
        //given
        productQuantityField= searchProduct.getClass().getDeclaredField("productQuantity");
        productQuantityField.setAccessible(true);
        productQuantityField.set(searchProduct, 20);
        //when
        final int result= searchProduct.getProductQuantity();
        //then
        assertEquals("test getProductQuantity", result, 20);
    }
    //test productPrice setter and getter
    @Test
    public void testSetProductPrice() throws NoSuchFieldException, IllegalAccessException {
        //when
        searchProduct.setPrice("99");
        //then
        priceField= searchProduct.getClass().getDeclaredField("price");
        priceField.setAccessible(true);
        assertEquals("test setPrice", priceField.get(searchProduct), "99");
    }
    @Test
    public void testGetProductPrice() throws NoSuchFieldException, IllegalAccessException {
        //given
        priceField= searchProduct.getClass().getDeclaredField("price");
        priceField.setAccessible(true);
        priceField.set(searchProduct, "79");
        //when
        final String result= searchProduct.getPrice();
        //then
        assertEquals("test getPrice", result, "79");
    }
    //test locationID setter and getter
    @Test
    public void testSetLocationID() throws NoSuchFieldException, IllegalAccessException {
        //when
        searchProduct.setLocationID("STR5");
        //then
        locationIDField= searchProduct.getClass().getDeclaredField("locationID");
        locationIDField.setAccessible(true);
        assertEquals("test setLocationID", locationIDField.get(searchProduct), "STR5");
    }
    @Test
    public void testGetLocationID() throws NoSuchFieldException, IllegalAccessException {
        //given
        locationIDField= searchProduct.getClass().getDeclaredField("locationID");
        locationIDField.setAccessible(true);
        locationIDField.set(searchProduct, "WRH23");
        //when
        final String result= searchProduct.getLocationID();
        //then
        assertEquals("test getLocationID", result, "WRH23");
    }
    //test locationName setter and getter
    @Test
    public void testSetLocationName() throws NoSuchFieldException, IllegalAccessException {
        //when
        searchProduct.setLocationName("Mosman Store");
        //then
        locationNameField= searchProduct.getClass().getDeclaredField("locationName");
        locationNameField.setAccessible(true);
        assertEquals("test setLocationName", locationNameField.get(searchProduct), "Mosman Store");
    }
    @Test
    public void testGetLocationName() throws NoSuchFieldException, IllegalAccessException {
        //given
        locationNameField= searchProduct.getClass().getDeclaredField("locationName");
        locationNameField.setAccessible(true);
        locationNameField.set(searchProduct, "Annandale Warehouse");
        //when
        final String result= searchProduct.getLocationName();
        //then
        assertEquals("test getLocationName", result, "Annandale Warehouse");
    }
    //test locationAddress setter and getter
    @Test
    public void testSetLocationAddress() throws NoSuchFieldException, IllegalAccessException {
        //when
        searchProduct.setLocationAddress("33 James St");
        //then
        locationAddressField= searchProduct.getClass().getDeclaredField("locationAddress");
        locationAddressField.setAccessible(true);
        assertEquals("test setLocationAddress", locationAddressField.get(searchProduct), "33 James St");
    }
    @Test
    public void testGetLocationAddress() throws NoSuchFieldException, IllegalAccessException {
        //given
        locationAddressField= searchProduct.getClass().getDeclaredField("locationAddress");
        locationAddressField.setAccessible(true);
        locationAddressField.set(searchProduct, "27 Liverpool St");
        //when
        final String result= searchProduct.getLocationAddress();
        //then
        assertEquals("test getLocationAddress", result, "27 Liverpool St");
    }
    //test phone setter and getter
    @Test
    public void testSetPhone() throws NoSuchFieldException, IllegalAccessException {
        //when
        searchProduct.setPhone("3456789");
        //then
        phoneField= searchProduct.getClass().getDeclaredField("phone");
        phoneField.setAccessible(true);
        assertEquals("test setPhone", phoneField.get(searchProduct), "3456789");
    }
    @Test
    public void testGetPhone() throws NoSuchFieldException, IllegalAccessException {
        //given
        phoneField= searchProduct.getClass().getDeclaredField("phone");
        phoneField.setAccessible(true);
        phoneField.set(searchProduct, "2345678");
        //when
        final String result= searchProduct.getPhone();
        //then
        assertEquals("test getPhone", result, "2345678");
    }

}