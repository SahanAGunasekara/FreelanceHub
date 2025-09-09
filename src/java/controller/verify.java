/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.AdvanceDetails;
import hibernate.HibernateUtil;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Mail;
import model.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


@WebServlet(name = "verify", urlPatterns = {"/verify"})
public class verify extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //System.out.println("request recieved");
        
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        
        HttpSession ses = request.getSession();
        final User u = (User)ses.getAttribute("user");
        if (u!=null) {
            final String genCode = Util.generateCode();
            
            //System.out.println(genCode);
            
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            
            AdvanceDetails adv = new AdvanceDetails();
            adv.setVerification(genCode);
            adv.setJoined(new Date());
            adv.setUser(u);
            s.save(adv);
            s.beginTransaction().commit();
            
            responseObject.addProperty("status", true);
            responseObject.addProperty("message", "verification code sent");
            
            new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Mail.sendMail(u.getEmail(), "FreelanceHub - Verification", "<h1>" + genCode + "</h1>");
                    }
                }).start();
            
        }else {
        
        }
        
        Gson gson = new Gson();
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
        
    }

    

}
