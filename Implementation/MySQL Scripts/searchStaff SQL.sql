select * from Staff;
delete  from Staff where userName= '';

select userName, firstName, lastName, locationID, contact, dateOfBirth, address, email
from Staff
where userName='' or firstName='Lek' or lastName='Maharjan';