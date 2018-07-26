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
    @Path("searchAccount/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SearchAccount> searchAccount(@QueryParam("user") String username, @QueryParam("user") String password) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        AccountDAO AccountDAO = new AccountDAO();

        System.out.println("search account called!");

        return AccountDAO.searchAccount(username, password);


    }



}
