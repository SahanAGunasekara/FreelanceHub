
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Gig;
import hibernate.HibernateUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


@WebServlet(name = "loadChatData", urlPatterns = {"/loadChatData"})
public class loadChatData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        
        Gig gig = (Gig)s.get(Gig.class, Integer.valueOf(id));
        
        gig.getFreelancer().getUser().setPassword("******");
        //gig.getFreelancer().getUser().setId(-1);
        
        responseObject.add("gigObject", gson.toJsonTree(gig));
        
        responseObject.addProperty("status", true);
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
    }

   
    

}
