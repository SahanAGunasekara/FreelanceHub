
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.AdvanceDetails;
import hibernate.Freelancer;
import hibernate.HibernateUtil;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;


@WebServlet(name = "freelancerData", urlPatterns = {"/freelancerData"})
public class freelancerData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String freelancerId = request.getParameter("frid");
        String userId = request.getParameter("uid");
        
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        
        Freelancer freelancer = (Freelancer)s.get(Freelancer.class, Integer.parseInt(freelancerId));
        User user = (User)s.get(User.class,Integer.parseInt(userId));
        
        Criteria c = s.createCriteria(AdvanceDetails.class);
        c.add(Restrictions.eq("user", user));
        
        AdvanceDetails userAdv = (AdvanceDetails)c.list().get(0);
        
        responseObject.add("freelancer", gson.toJsonTree(freelancer));
        responseObject.add("userAdv", gson.toJsonTree(userAdv));
        
        responseObject.addProperty("status", true);
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
    }

    

}
