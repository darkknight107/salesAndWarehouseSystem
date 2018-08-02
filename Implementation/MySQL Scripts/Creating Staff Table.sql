#Creating a table with its different attributes 
Create Table Staff(
userName varchar(20) not null,
pWord char(128) not null,
firstName varchar(45) not null,
middleName varchar(45),
lastName varchar(45) not null,
locationID varchar(20),
contact varchar(20),
dateOfBirth varchar(20),
address varchar(45) not null,
email varchar(30) not null,
primary key (userName),
foreign key (locationID) references Location(locationID)
on delete cascade
on update  cascade);

update Staff Set pWord= SHA2(pWord, 512);

#adding a new staff wth password hashed using sha512
Insert into Staff(userName, pWord, firstName, middleName, lastName, 
locationID, contact, dateOfBirth, address, email)
values('Matt', sha2('abcd1234', 512), 'Hanh', 'Hieu', 'Tran', 'STR1', '5540900', '31/12/1994', 
'32 Preston Street, Eastwood NSW 2112', 'hanhtran@gmail.com');

select * from Staff;

select pWord
from Staff
where userName='Matt';
