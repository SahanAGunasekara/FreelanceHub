
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Gig;
import hibernate.GigStatus;
import hibernate.HibernateUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


@WebServlet(name = "approveGig", urlPatterns = {"/approveGig"})
public class approveGig extends HttpServlet {
        private static final int STATUS=2;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String gigId = request.getParameter("id");
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        
        if (gigId.isEmpty()) {
            responseObject.addProperty("message", "Please try again");
        }else{
            if (Util.isInteger(gigId)) {
                SessionFactory sf = HibernateUtil.getSessionFactory();
                Session s = sf.openSession();
                
                GigStatus status = (GigStatus)s.get(GigStatus.class, STATUS);
                Gig gig = (Gig)s.get(Gig.class, Integer.parseInt(gigId));
                if (gig.getStatus().equals(status)) {
                    responseObject.addProperty("message", "Gig already in Approved status");
                }else{
                    gig.setStatus(status);
                    s.update(gig);
                    s.beginTransaction().commit();
                    responseObject.addProperty("message", "Gig status updated successfully");
                    responseObject.addProperty("status", true);
                    
                }
                
            }else{
                responseObject.addProperty("message", "Please try again");
            }
        }
        
            String toJson = gson.toJson(responseObject);
            response.setContentType("application/json");
            response.getWriter().write(toJson);
    }

    

}
