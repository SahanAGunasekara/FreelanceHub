
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Chat;
import hibernate.Gig;
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
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;


@WebServlet(name = "loadMessage", urlPatterns = {"/loadMessage"})
public class loadMessage extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        JsonObject requestJson = gson.fromJson(request.getReader(), JsonObject.class);
        
        String rid = requestJson.get("recieverId").getAsString();
        String gid = requestJson.get("gigId").getAsString();
        
        HttpSession ses = request.getSession();
        User u = (User)ses.getAttribute("user");
        
        if (u!=null) {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            
            User reciever = (User)s.get(User.class, Integer.valueOf(rid));
            Gig gig = (Gig)s.get(Gig.class, Integer.valueOf(gid));
            
            
            Criteria c1 = s.createCriteria(Chat.class);
            c1.add(Restrictions.eq("userSender", u));
            c1.add(Restrictions.eq("userReciever", reciever));
            c1.add(Restrictions.eq("gig", gig));
            
            Criteria c2 = s.createCriteria(Chat.class);
            c2.add(Restrictions.eq("userSender", reciever));
            c2.add(Restrictions.eq("userReciever", u));
            c2.add(Restrictions.eq("gig", gig));
            
            List<Chat> senderChatList = c1.list();
            List<Chat> recieverChatList = c2.list();
            
            responseObject.add("senderChatList", gson.toJsonTree(senderChatList));
            responseObject.add("recieverChatList", gson.toJsonTree(recieverChatList));
            
            
            responseObject.addProperty("status", true);
        }else{
            responseObject.addProperty("message", "please signIn");
        }
        
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
    }

   

}
