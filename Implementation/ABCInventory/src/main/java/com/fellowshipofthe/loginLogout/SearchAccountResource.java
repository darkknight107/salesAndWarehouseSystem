package com.fellowshipofthe.loginLogout;

import com.fellowshipofthe.entityClasses.SearchAccount;
import com.fellowshipofthe.DAO.AccountDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.List;

@Path("searchAccount")
public class SearchAccountResource{


    @GET
    @Path("/account")
    @Produces(MediaType.TEXT_PLAIN)
    public String searchAccount(@QueryParam("username") String username, @QueryParam ("password") String password) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        AccountDAO accountDAO = new AccountDAO();

        System.out.println("search account called!");

        return accountDAO.searchAccount (username, password);

    }
}
