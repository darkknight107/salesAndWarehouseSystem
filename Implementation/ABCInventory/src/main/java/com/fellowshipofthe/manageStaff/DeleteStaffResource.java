package com.fellowshipofthe.manageStaff;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("managestaff")
public class DeleteStaffResource {

    @DELETE
    @Path("deletestaff/{userName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteStaff(@PathParam("userName") String userName){
        System.out.println("deleteStaff called!");
    }
}
