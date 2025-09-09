
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
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;


@WebServlet(name = "updateNewPassword", urlPatterns = {"/updateNewPassword"})
public class updateNewPassword extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        
        JsonObject requestJson = gson.fromJson(request.getReader(), JsonObject.class);
        String code = requestJson.get("passCode").getAsString();
        String newPassword = requestJson.get("newPasswrd").getAsString();
        String confirmPassword = requestJson.get("confPasswrd").getAsString();
        
//        System.out.println(code);
//        System.out.println(newPassword);
//        System.out.println(confirmPassword);

        if (code.isEmpty()) {
            responseObject.addProperty("message", "request code cannot be empty");
        }else if(newPassword.isEmpty()){
            responseObject.addProperty("message", "New password cannot be empty");
        }else if(!Util.isPasswordValid(newPassword)){
            responseObject.addProperty("message", "Please enter valid password");
        }else if(!newPassword.equals(confirmPassword)){
            responseObject.addProperty("message", "Confirm Password doesn't match");
        }else{
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            
            HttpSession ses = request.getSession();
            User u = (User)ses.getAttribute("user");
            
            Criteria c = s.createCriteria(User.class);
            c.add(Restrictions.eq("email", u.getEmail()));
            
            if (!c.list().isEmpty()) {
                User user = (User)c.uniqueResult();
                
                if (user.getpCode().equals(code)) {
                    user.setPassword(newPassword);
                    
                    s.update(user);
                    
                    s.beginTransaction().commit();
                    responseObject.addProperty("status", true);
                    
                }else{
                    responseObject.addProperty("message", "Incorrect request Code");
                }
                
                
            }else{
                responseObject.addProperty("message", "Please SignIn again");
            }
        }
        
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
        
    }

    

}
