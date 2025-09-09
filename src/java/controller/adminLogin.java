package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Admin;
import hibernate.HibernateUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Mail;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "adminLogin", urlPatterns = {"/adminLogin"})
public class adminLogin extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        JsonObject requestObject = gson.fromJson(request.getReader(), JsonObject.class);

        String email = requestObject.get("username").getAsString();
        String password = requestObject.get("password").getAsString();

        if (email.isEmpty()) {
            
            responseObject.addProperty("message", "Email cannot be null");
        } else if (!Util.isEmailValid(email)) {
            responseObject.addProperty("message", "Please enter valid email");
        } else if (password.isEmpty()) {
            responseObject.addProperty("message", "Password cannot be empty");
        } else {
            
            String genCode = Util.generateCode();
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            Criteria c1 = s.createCriteria(Admin.class);
            c1.add(Restrictions.eq("email", email));
            c1.add(Restrictions.eq("password", password));

            if (!c1.list().isEmpty()) {
                
                Admin admin = (Admin)c1.uniqueResult();
                admin.setEmail(email);
                admin.setVerification(genCode);
                s.update(admin);
                s.beginTransaction().commit();
                
                Admin admin1 = new Admin();
                admin1.setEmail(email);
                admin1.setVerification(genCode);
                
                HttpSession ses = request.getSession();
                ses.setAttribute("admin", admin1);
                ses.setMaxInactiveInterval(3600);
                
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Mail.sendMail(admin.getEmail(), "FreelanceHub - Admin Verification", "<h1>" + genCode + "</h1>");
                    }
                }).start();
                
                
                responseObject.addProperty("message", "Verification code sent Successfully");
                responseObject.addProperty("status", true);
            } else {
                responseObject.addProperty("message", "Invalid email address or password");
            }

        }

        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);

    }

}
