package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtil;
import hibernate.Rtype;
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

@WebServlet(name = "UserRegister", urlPatterns = {"/UserRegister"})
public class UserRegister extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //System.out.println("Request recieved");
        Gson gson = new Gson();
        JsonObject user = gson.fromJson(request.getReader(), JsonObject.class);

        String userName = user.get("userName").getAsString();
        String email = user.get("email").getAsString();
        String password = user.get("password").getAsString();
        String confirmPassword = user.get("confirmPassword").getAsString();
        int userRole = user.get("userRole").getAsInt();
        
//        System.out.println(userName);
//        System.out.println(email);
//        System.out.println(password);
//        System.out.println(confirmPassword);
//        System.out.println(userRole);
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (userName.isEmpty()) {
            responseObject.addProperty("message", "Username Cannot be Empty");
        } else if (email.isEmpty()) {
            responseObject.addProperty("message", "Email Cannot be Empty");
        } else if (!Util.isEmailValid(email)) {
            responseObject.addProperty("message", "Please enter valid email");
        } else if (password.isEmpty()) {
            responseObject.addProperty("message", "Password field cannot be empty");
        } else if (!Util.isPasswordValid(password)) {
            responseObject.addProperty("message", "Please enter valid password");
        } else if (confirmPassword.isEmpty()) {
            responseObject.addProperty("message", "Please re-enter password");
        } else if (!password.equals(confirmPassword)) {
            responseObject.addProperty("message", "Passwords doesn't match");
        } else if (userRole==0) {
            responseObject.addProperty("message", "Please select user Type");
        } else {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            Criteria c = s.createCriteria(User.class);
            c.add(Restrictions.eq("email", email));
            
            if (!c.list().isEmpty()) {
                responseObject.addProperty("message", "User with this email already exists");
            }else{
                Rtype r = (Rtype) s.load(Rtype.class, userRole);

                User u = new User();
                u.setUsername(userName);
                u.setEmail(email);
                u.setPassword(password);
                u.setRole(r);

                s.save(u);
                s.beginTransaction().commit();
                
                HttpSession ses = request.getSession();
                ses.setAttribute("user", u);

                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "User Successfully Registered");
            }
        }
        

        
        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);

    }

}
