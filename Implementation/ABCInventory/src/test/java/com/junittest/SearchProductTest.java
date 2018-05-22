package com.junittest;

import com.fellowshipofthe.SearchProduct;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static junit.framework.TestCase.assertEquals;

public class SearchProductTest {
    SearchProduct searchProduct= null;
    Field productItemField= null;


    @Before
    public void setUp() throws Exception {
        searchProduct= new SearchProduct();
    }
    @Test
    public void testSetProductItemCode() throws NoSuchFieldException, IllegalAccessException {
        //when
        searchProduct.setProductItemCode("A1");

        //then
        productItemField= searchProduct.getClass().getDeclaredField("productItemCode");
        productItemField.setAccessible(true);
        assertEquals("Testing setProductItemCode", productItemField.get(searchProduct), "A1");
    }

    @Test
    public void testGetProductItemCode() throws NoSuchFieldException, IllegalAccessException {
        //given
        productItemField = searchProduct.getClass().getDeclaredField("productItemCode");
        productItemField.setAccessible(true);
        productItemField.set(searchProduct, "A1");

        //when
        final String result= searchProduct.getProductItemCode();

        //then
        assertEquals("testing getProductItemCode", result, "A1");
    }
}