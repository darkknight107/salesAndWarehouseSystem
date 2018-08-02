package com.fellowshipofthe.manageStaff;

import com.fellowshipofthe.DAO.StaffDAO;
import com.fellowshipofthe.entityClasses.Staff;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

@Path("managestaff")
public class AddStaffResource {
    StaffDAO staffDAO= new StaffDAO();

    @POST
    @Path("addstaff")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addStaff(Staff newStaff) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        System.out.println("addStaff called!");
        //calling staffDAO to connect with database and insert new staff
        return staffDAO.addStaff(newStaff);
    }
}
