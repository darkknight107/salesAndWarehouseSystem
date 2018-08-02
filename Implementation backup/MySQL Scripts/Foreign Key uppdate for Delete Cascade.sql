#Dropping foreign key (constraint) productCode in table ProductItem
Alter Table ProductItem Drop Foreign Key ProductItem_ibfk_1;

#Adding foreign key (constraint) productCode with delete cascade(deletes all entry with delete query) in table ProductItem
Alter Table ProductItem
Add constraint ProductItem_ibfk_1
foreign key (productCode) references Product (productCode)
on delete cascade;

#Dropping foreign key (constraint) locationID
Alter Table Transfer Drop Foreign Key Transfer_ibfk_1;

#Adding foeign key (constraint) locationID with delete cascade
Alter Table Transfer
Add constraint Transfer_ibfk_1
foreign key (sendingLocationID) references StoredProduct(locationID)
on delete cascade;

#Dropping foreign key (constraint) transferID
Alter Table TransferItem Drop Foreign Key TransferItem_ibfk_1;

#Adding foreign key (constraint) transferID with delete cascade
Alter Table TransferItem
Add constraint TransferItem_ibfk_1
foreign key (transferID) references Transfer(transferID)
on delete cascade;

#Dropping foreign key (constraint) productItemCode in table StoredProduct
Alter Table StoredProduct
drop foreign key StoredProduct_ibfk_1;

#Dropping foreign key (constraint) locationID in table StoredProduct
Alter Table StoredProduct
drop foreign key StoredProduct_ibfk_2;

#Adding foreign key (constraint) productItemCode in table Stored product with delete cascade
Alter Table StoredProduct
add constraint StoredProduct_ibfk_1
foreign key (productItemCode) references ProductItem (productItemCode)
on delete cascade;

#Adding foreign key (constraint) locationID in table Stored product with delete cascade
Alter Table StoredProduct
add constraint StoredProduct_ibfk_2
foreign key (locationID) references Location (locationID)
on delete cascade;

select * from Product;
select * from ProductItem;

#Delete a product with certain product code
delete from Product
where productCode='Z1';