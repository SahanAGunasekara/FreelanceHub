package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Chat;
import hibernate.Gig;
import hibernate.HibernateUtil;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@WebServlet(name = "saveMessage", urlPatterns = {"/saveMessage"})
public class saveMessage extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        JsonObject requestDetails = gson.fromJson(request.getReader(), JsonObject.class);

        String msg = requestDetails.get("msgText").getAsString();
        String reciever = requestDetails.get("recieverID").getAsString();
        String gigId = requestDetails.get("GigId").getAsString();

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        HttpSession ses = request.getSession();
        User u = (User) ses.getAttribute("user");

        if (u != null) {

            if (reciever.isEmpty()) {
                responseObject.addProperty("message", "Reciever is null");
            } else if (gigId.isEmpty()) {
                responseObject.addProperty("message", "Something went wrong");
            } else if (msg.isEmpty()) {
                responseObject.addProperty("message", "please enter message");
            } else {
                User reciverOb = (User) s.get(User.class, Integer.valueOf(reciever));
                Gig gigOb = (Gig) s.get(Gig.class, Integer.parseInt(gigId));
                
                try {
                    
                
                Chat chat = new Chat();
                chat.setUserSender(u);
                chat.setUserReciever(reciverOb);
                chat.setTime(new Timestamp(System.currentTimeMillis()));
                chat.setMessage(msg);
                chat.setGig(gigOb);
                
                s.save(chat);
                s.beginTransaction().commit();
                
                } catch (Exception e) {
                    responseObject.addProperty("message", "can be timestamp error");
                }

                responseObject.addProperty("status", true);
            }

        } else {
            responseObject.addProperty("message", "Please signIn");
        }

        
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);

    }

}
