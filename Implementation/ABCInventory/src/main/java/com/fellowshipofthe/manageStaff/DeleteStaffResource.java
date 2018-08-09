package com.fellowshipofthe.manageStaff;

import com.fellowshipofthe.DAO.StaffDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

@Path("managestaff")
public class DeleteStaffResource {

    @DELETE
    @Path("deletestaff/{userName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean deleteStaff(@PathParam("userName") String userName) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        System.out.println("deleteStaff called!");
        StaffDAO staffDAO= new StaffDAO();
        return staffDAO.deleteStaff(userName);
    }
}
