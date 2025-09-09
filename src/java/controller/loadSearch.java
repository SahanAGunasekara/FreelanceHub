
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Category;
import hibernate.Country;
import hibernate.Gig;
import hibernate.HibernateUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;


@WebServlet(name = "loadSearch", urlPatterns = {"/loadSearch"})
public class loadSearch extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //System.out.println("Request recieved");
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        
        Criteria c1 = s.createCriteria(Gig.class);
        c1.addOrder(Order.desc("id"));
        responseObject.addProperty("gigCount", c1.list().size());
        c1.setFirstResult(0);
        c1.setMaxResults(6);
        
        List<Gig> gigList = c1.list();
        
        for (Gig gig : gigList) {
            gig.getFreelancer().getUser().setId(-1);
            gig.getFreelancer().getUser().setEmail("example@gmail.com");
            gig.getFreelancer().getUser().setPassword("********");
            gig.getFreelancer().getUser().setRole(null);
        }
        
        
        
        Criteria c2 = s.createCriteria(Category.class);
        List<Category> categoryList = c2.list();
        
        
        Criteria c3 = s.createCriteria(Country.class);
        List<Country> countryList = c3.list();
        
        responseObject.addProperty("status", true);
        responseObject.add("categoryList", gson.toJsonTree(categoryList));
        responseObject.add("gigList", gson.toJsonTree(gigList));
        
        responseObject.add("countryList", gson.toJsonTree(countryList));
        
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
    }

    

}
