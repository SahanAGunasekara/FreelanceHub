
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Freelancer;
import hibernate.Gig;
import hibernate.GigStatus;
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
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;


@WebServlet(name = "loadAdminPageData", urlPatterns = {"/loadAdminPageData"})
public class loadAdminPageData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //System.out.println("request recieved");
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        
        Criteria c1 = s.createCriteria(User.class);
        List<User> userList = c1.list();
        
        for (User user : userList) {
            user.setPassword("**********");
        }
        
        Criteria c2 = s.createCriteria(Freelancer.class);
        List<Freelancer> freelancerList = c2.list();
        
        Criteria c3 = s.createCriteria(Gig.class);
        List<Gig> gigList = c3.list();
        
        GigStatus gstatus = (GigStatus)s.load(GigStatus.class, 2);
        
        Criteria c4 = s.createCriteria(Gig.class);
        c4.add(Restrictions.eq("status", gstatus));
        
        
        
        responseObject.addProperty("activeGigCount", c4.list().size());
        responseObject.addProperty("userCount", c1.list().size());
        responseObject.addProperty("freelancerCount", c2.list().size());
        responseObject.add("freelancerList", gson.toJsonTree(freelancerList));
        responseObject.add("userList", gson.toJsonTree(userList));
        responseObject.add("gigList", gson.toJsonTree(gigList));
        
        responseObject.addProperty("status", true);
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
    }

    

}
