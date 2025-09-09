
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.AdvanceDetails;
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


@WebServlet(name = "verifyAccount", urlPatterns = {"/verifyAccount"})
public class verifyAccount extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        
        String verifyCode = request.getParameter("code");
        
        HttpSession ses = request.getSession();
        User u = (User)ses.getAttribute("user");
        
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        
        Criteria c = s.createCriteria(AdvanceDetails.class);
        c.add(Restrictions.eq("user", u));
        
        if (!c.list().isEmpty()) {
            AdvanceDetails ad = (AdvanceDetails)c.uniqueResult();
            if (ad.getVerification().equals(verifyCode)) {
                ad.setVerification("verified");
                
                s.update(ad);
                s.beginTransaction().commit();
                responseObject.addProperty("status", true);
            }else{
                responseObject.addProperty("message", "Incorrect verification code");
            }
        }else{
            responseObject.addProperty("message", "Please send verification code first");
        }
        
        Gson gson = new Gson();
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
        
    }

   

}
