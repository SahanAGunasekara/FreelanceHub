
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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


@WebServlet(name = "loadProfileData", urlPatterns = {"/loadProfileData"})
public class loadProfileData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        
        HttpSession ses = request.getSession();
        User u = (User)ses.getAttribute("user");
        
        if (u!=null) {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            
            Criteria c = s.createCriteria(User.class);
            c.add(Restrictions.eq("email", u.getEmail()));
            if (c.list().isEmpty()) {
                responseObject.addProperty("message", "Please check again");
            }else{
                User u1 = (User)c.list().get(0);
                u1.setPassword("*******");
                responseObject.add("userPData", gson.toJsonTree(u1));
                responseObject.addProperty("status", true);
            }
        }else{
            responseObject.addProperty("message", "Please signIn");
        }
        
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
        
    }

    

}
