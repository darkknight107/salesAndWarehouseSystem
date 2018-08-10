package com.fellowshipofthe.manageStaff;

import com.fellowshipofthe.DAO.ProductDAO;
import com.fellowshipofthe.DAO.StaffDAO;
import com.fellowshipofthe.entityClasses.Product;
import com.fellowshipofthe.entityClasses.Staff;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
@Path("managestaff")
public class EditStaffResource {
    StaffDAO staffDAO;
    public EditStaffResource(){
        staffDAO= new StaffDAO();
    }

    @Path("updatestaff")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean updateStaff(Staff updatedStaff) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        System.out.println("updateProduct called!");
        return staffDAO.updateStaff(updatedStaff);
    }

    @Path("/updatepassword")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean updatePassword(@QueryParam("username") String userName, @QueryParam("password") String password) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        System.out.println("updatePassword called!");
        System.out.println(userName + " " + password);
        return staffDAO.updatePassword(userName, password);
    }
}
