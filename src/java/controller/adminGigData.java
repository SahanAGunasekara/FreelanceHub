
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Gig;
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


@WebServlet(name = "adminGigData", urlPatterns = {"/adminGigData"})
public class adminGigData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String gigId = request.getParameter("id");
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        
        if (!Util.isInteger(gigId)) {
            responseObject.addProperty("message", "Please try again");
        }else{
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            
            Gig gig = (Gig)s.get(Gig.class, Integer.parseInt(gigId));
            
            responseObject.add("gigData", gson.toJsonTree(gig));
            
            responseObject.addProperty("status", true);
            
        }
        
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
        
        
                
    }

    

}
