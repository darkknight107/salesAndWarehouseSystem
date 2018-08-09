package com.fellowshipofthe.manageStaff;

import com.fellowshipofthe.DAO.StaffDAO;
import com.fellowshipofthe.entityClasses.Staff;

import javax.ws.rs.*;
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

    @GET
    @Path("searchstaff")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Staff> searchStaff(@QueryParam("name") String name) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        System.out.println("searchStaff called!");
        return staffDAO.searchStaff(name);
    }

}
