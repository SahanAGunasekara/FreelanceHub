
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Freelancer;
import hibernate.HibernateUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


@WebServlet(name = "activateFreelancer", urlPatterns = {"/activateFreelancer"})
public class activateFreelancer extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String freelancerId = request.getParameter("id");
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        
        if (freelancerId.isEmpty()) {
            responseObject.addProperty("message", "Please try again");
        }else{
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            
            Freelancer freelancer = (Freelancer)s.get(Freelancer.class, Integer.valueOf(freelancerId));
            
            if (freelancer.getAvailability().equals("Active")) {
                responseObject.addProperty("message", "Freelancer already in active status");
            }else{
                freelancer.setAvailability("Active");
                s.update(freelancer);
                s.beginTransaction().commit();
                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "Freelancer Successfully Activated");
            }
        }
        
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
    }

   

}
