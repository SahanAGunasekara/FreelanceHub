
package controller;

import hibernate.FavouriteGigs;
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
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;


@WebServlet(name = "addSesssionFav", urlPatterns = {"/addSesssionFav"})
public class addSesssionFav extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession ses = request.getSession();
        User u = (User)ses.getAttribute("user");
        
        if (u != null) {
            ArrayList<FavouriteGigs> fg = (ArrayList<FavouriteGigs>)ses.getAttribute("sessionFav");
            if (fg!=null) {
                
                SessionFactory sf = HibernateUtil.getSessionFactory();
                Session s = sf.openSession();
                
                Criteria c1 = s.createCriteria(FavouriteGigs.class);
                c1.add(Restrictions.eq("user", u));
                
                for (FavouriteGigs favouriteGig : fg) {
                    c1.add(Restrictions.eq("gig", favouriteGig.getGig()));
                    
                    if (c1.list().isEmpty()) {
                        favouriteGig.setUser(u);
                        s.save(favouriteGig);
                        s.beginTransaction().commit();
                    }
                }
                
                request.getSession().setAttribute("sessionFav",null);
            }
        }
    }

   

}
