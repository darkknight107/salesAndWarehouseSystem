update Product set productCode= replace(productCode, "100", "S1");
update Product set productCode= replace(productCode, "200", "S2");

-- Search Product query
SELECT p.productCode, 
        p.productName, 
        sum(sp.productQuantity) 'productQuantity',
        p.price,
        sp.locationID,
        l.locationName,
        l.locationAddress, 
        l.phone
FROM StoredProduct sp
	JOIN ProductItem pi ON sp.productItemCode = pi.productItemCode
    JOIN Product p ON pi.productCode = p.productCode
    JOIN Location l ON sp.locationID = l.locationID
WHERE p.productCode = "S1"
group by l.locationID;