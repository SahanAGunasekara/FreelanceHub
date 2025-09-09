package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.FavouriteGigs;
import hibernate.Gig;
import hibernate.HibernateUtil;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "AddToFav", urlPatterns = {"/AddToFav"})
public class AddToFav extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        if (!Util.isInteger(id)) {
            responseObject.addProperty("message", "Please add valid gig");
        } else {

            HttpSession ses = request.getSession();
            User u = (User) ses.getAttribute("user");
            if (u != null) {

                Gig gig = (Gig) s.get(Gig.class, Integer.parseInt(id));

                if (gig == null) {
                    responseObject.addProperty("message", "Invalid Gig");
                } else {
                    Criteria c1 = s.createCriteria(FavouriteGigs.class);
                    c1.add(Restrictions.eq("user", u));
                    c1.add(Restrictions.eq("gig", gig));

                    if (c1.list().isEmpty()) {
                        FavouriteGigs fg = new FavouriteGigs();
                        fg.setUser(u);
                        fg.setGig(gig);

                        s.save(fg);
                        s.beginTransaction().commit();
                        responseObject.addProperty("status", true);
                        responseObject.addProperty("message", "Gig Added to favourites successfully");
                    } else {
                        responseObject.addProperty("message", "Gig Already added to favourites");
                    }

                }

            } else {
                //responseObject.addProperty("message", "Inactive session but can add to fav");

                if (ses.getAttribute("sessionFav") == null) {

                    Gig gig = (Gig) s.get(Gig.class, Integer.parseInt(id));
                    ArrayList<FavouriteGigs> sessionFavourite = new ArrayList<>();
                    FavouriteGigs fg2 = new FavouriteGigs();
                    fg2.setUser(null);
                    fg2.setGig(gig);

                    sessionFavourite.add(fg2);
                    ses.setAttribute("sessionFav", sessionFavourite);
                    responseObject.addProperty("status", true);
                    responseObject.addProperty("message", "Gig Added to Session favourites successfully");
                } else {
                    ArrayList<FavouriteGigs> sesArr = (ArrayList<FavouriteGigs>) ses.getAttribute("sessionFav");
                    FavouriteGigs Favgig = null;
                    for (FavouriteGigs favouriteGig : sesArr) {
                        if (favouriteGig.getGig().getId() == Integer.parseInt(id)) {
                            Favgig = favouriteGig;
                            break;
                        }
                    }

                    if (Favgig != null) {
                        responseObject.addProperty("status", false);
                        responseObject.addProperty("message", "Gig Already added to session favourites");
                    } else {
                        Gig gig = (Gig) s.load(Gig.class, Integer.valueOf(id));
                        FavouriteGigs fg3 = new FavouriteGigs();
                        fg3.setUser(null);
                        fg3.setGig(gig);
                        Favgig = fg3;
                        sesArr.add(Favgig);
                        responseObject.addProperty("status", true);
                        responseObject.addProperty("message", "Gig Added to Session favourites successfully");
                    }
                }
            }

        }

        Gson gson = new Gson();
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);

    }

}
