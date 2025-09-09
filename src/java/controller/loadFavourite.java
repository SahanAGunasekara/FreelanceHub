package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.FavouriteGigs;
import hibernate.HibernateUtil;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "loadFavourite", urlPatterns = {"/loadFavourite"})
public class loadFavourite extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //System.out.println("Request recieved");
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        HttpSession ses = request.getSession();
        User u = (User) ses.getAttribute("user");

        if (u != null) {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            Criteria c = s.createCriteria(FavouriteGigs.class);
            c.add(Restrictions.eq("user", u));
            List<FavouriteGigs> favList = c.list();

            if (favList.isEmpty()) {
                responseObject.addProperty("message", "Please add items to Favourites to list");
            } else {
                responseObject.addProperty("status", true);
                responseObject.add("favouriteList", gson.toJsonTree(favList));
                
            }

        } else {
            responseObject.addProperty("message", "Please SignIn to watch Favourite list");
        }

        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
    }

}
