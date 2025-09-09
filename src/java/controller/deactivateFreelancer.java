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

@WebServlet(name = "deactivateFreelancer", urlPatterns = {"/deactivateFreelancer"})
public class deactivateFreelancer extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String freelancerId = request.getParameter("id");

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        

        if (freelancerId.isEmpty()) {
            responseObject.addProperty("message", "Please try again");
        } else {
            
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            Freelancer freelancer = (Freelancer) s.load(Freelancer.class, Integer.valueOf(freelancerId));

            if (freelancer.getAvailability().equals("Deactive")) {
                responseObject.addProperty("message", "Freelancer already in deactive status");
            } else {
                freelancer.setAvailability("Deactive");
                s.update(freelancer);
                s.beginTransaction().commit();
                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "Freelancer availability updated successfully");
            }

        }
        
        Gson gson = new Gson();
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);

    }

}
