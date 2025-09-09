
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
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;


@WebServlet(name = "verifyAdmin", urlPatterns = {"/verifyAdmin"})
public class verifyAdmin extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        
        HttpSession ses = request.getSession();
        Admin admin = (Admin)ses.getAttribute("admin");
        
        if (admin != null) {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            
            Criteria c1 = s.createCriteria(Admin.class);
            c1.add(Restrictions.eq("email", admin.getEmail()));
            c1.add(Restrictions.eq("verification", id));
            
            if (!c1.list().isEmpty()) {
                responseObject.addProperty("status", true);
            }else{
                responseObject.addProperty("message", "Incorrect verification code");
            }
        }else{
            responseObject.addProperty("message", "Please signIn again");
            System.out.println("Please signIn again");
        }
        
        Gson gson = new Gson();
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
    }

   

}
