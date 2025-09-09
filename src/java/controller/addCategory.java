
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Admin;
import hibernate.Category;
import hibernate.HibernateUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.stat.internal.CategorizedStatistics;


@WebServlet(name = "addCategory", urlPatterns = {"/addCategory"})
public class addCategory extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        JsonObject requestObject = gson.fromJson(request.getReader(), JsonObject.class);
        
        String categoryName = requestObject.get("category").getAsString();
        String password = requestObject.get("password").getAsString();
        
        if (categoryName.isEmpty()) {
            responseObject.addProperty("message", "Category name cannot be empty");
        }else if(password.isEmpty()){
            responseObject.addProperty("message", "Please enter your admin password");
        }else{
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            
            Criteria c1 = s.createCriteria(Admin.class);
            c1.add(Restrictions.eq("password", password));
            
            if (!c1.list().isEmpty()) {
                Category category = new Category();
                category.setName(categoryName);
                
                s.save(category);
                s.beginTransaction().commit();
                
                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "Category saved successfully");
            }else{
                responseObject.addProperty("message", "Incorrect Admin password");
            }
        }
        
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
    }

   

}
