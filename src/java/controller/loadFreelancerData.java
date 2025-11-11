
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;


@WebServlet(name = "loadFreelancerData", urlPatterns = {"/loadFreelancerData"})
public class loadFreelancerData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //System.out.println("freelnacer Data loading");
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        
        HttpSession ses = request.getSession();
        User u = (User)ses.getAttribute("user");
        
        if (u!=null) {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            Criteria c1 = s.createCriteria(Freelancer.class);
            c1.add(Restrictions.eq("user", u));
            
            responseObject.add("freelancer", gson.toJsonTree(c1.list().get(0)));
            responseObject.addProperty("status", true);
        }else{
            responseObject.addProperty("message", "please login again");
        }
        
        
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
        
    }

    

}
