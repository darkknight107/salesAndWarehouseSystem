package com.fellowshipofthe.manageStaff;

import com.fellowshipofthe.DAO.StaffDAO;
import com.fellowshipofthe.entityClasses.Staff;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.List;

@Path("managestaff")
public class SearchStaffResource {
    StaffDAO staffDAO= new StaffDAO();
    @GET
    @Path("viewallstaff")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Staff> viewAllStaff() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        System.out.println("viewAllStaff called!");
        return staffDAO.viewAllStaff();
    }

}
