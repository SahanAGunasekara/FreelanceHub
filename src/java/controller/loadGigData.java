
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;


@WebServlet(name = "loadGigData", urlPatterns = {"/loadGigData"})
public class loadGigData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        
        if (!Util.isInteger(id)) {
            responseObject.addProperty("message", "product not found");
        }else{
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            
            Gig gig = (Gig)s.get(Gig.class, Integer.valueOf(id));
            
            responseObject.add("gigData", gson.toJsonTree(gig));
            
             Criteria c = s.createCriteria(Gig.class);
            c.add(Restrictions.eq("category", gig.getCategory()));
            c.add(Restrictions.ne("id", gig.getId()));
            List<Gig> gigList = c.list();
            
            for (Gig gig1 : gigList) {
                gig1.getFreelancer().getUser().setId(0);
                gig1.getFreelancer().getUser().setPassword("******");
                gig1.getFreelancer().getUser().setRole(null);
            }
            
            responseObject.addProperty("status", true);
            responseObject.add("gigList", gson.toJsonTree(gigList));
        }
        
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
    }

   
    

}
