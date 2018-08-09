package com.fellowshipofthe.manageStaff;

import com.fellowshipofthe.DAO.ProductDAO;
import com.fellowshipofthe.DAO.StaffDAO;
import com.fellowshipofthe.entityClasses.Product;
import com.fellowshipofthe.entityClasses.Staff;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
@Path("managestaff")
public class EditStaffResource {

        @Path("updatestaff")
        @PUT
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public Boolean updateStaff(Staff updatedStaff) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
            StaffDAO staffDAO = new StaffDAO();
            System.out.println("updateProduct called!");
            return staffDAO.updateStaff(updatedStaff);
        }
}
