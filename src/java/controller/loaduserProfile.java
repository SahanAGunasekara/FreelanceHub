
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.AdvanceDetails;
import hibernate.Category;
import hibernate.Country;
import hibernate.Freelancer;
import hibernate.HibernateUtil;
import hibernate.User;
import hibernate.UserDetails;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;
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


@WebServlet(name = "loaduserProfile", urlPatterns = {"/loaduserProfile"})
public class loaduserProfile extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //System.out.println("Request recieved");
        
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        
        HttpSession ses = request.getSession();
        User u = (User)ses.getAttribute("user");
        
        if (u != null) {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            
            Criteria c1 = s.createCriteria(User.class);
            c1.add(Restrictions.eq("email", u.getEmail()));
            
            Criteria c2 = s.createCriteria(Country.class);
            List<Country> Clist = c2.list();
            
            Criteria c3 = s.createCriteria(AdvanceDetails.class);
            c3.add(Restrictions.eq("user", u));
            
            
            Criteria c4 = s.createCriteria(UserDetails.class);
            c4.add(Restrictions.eq("user", u));
            
            if (!c1.list().isEmpty()) {
                User user = (User)c1.uniqueResult();
                responseObject.addProperty("userId", user.getId());
                responseObject.addProperty("userName", user.getUsername());
                responseObject.addProperty("email", user.getEmail());
                responseObject.add("countryList", gson.toJsonTree(Clist));
                
                responseObject.addProperty("status", true);
            }
            
            if (!c3.list().isEmpty()) {
                AdvanceDetails adv = (AdvanceDetails)c3.uniqueResult();
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
                String date = sdf.format(adv.getJoined());
                responseObject.addProperty("verifyStatus", adv.getVerification());
                responseObject.addProperty("joined", date);
                
                responseObject.addProperty("status", true);
            }
            
            if(!c4.list().isEmpty()){
                UserDetails ud = (UserDetails)c4.uniqueResult();
                
                responseObject.addProperty("mobile", ud.getMobile());
                responseObject.addProperty("bio", ud.getBio());
                responseObject.addProperty("cid", ud.getCountry().getId());
                
                responseObject.addProperty("status", true);
                
            }
            
        }else{
            responseObject.addProperty("message", "please SignIn again");
        }
        
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
    }

   

}
