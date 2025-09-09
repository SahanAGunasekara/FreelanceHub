
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Gig;
import hibernate.HibernateUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;


@WebServlet(name = "loadSearchIndex", urlPatterns = {"/loadSearchIndex"})
public class loadSearchIndex extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("key");
        
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        
        Criteria c = s.createCriteria(Gig.class);
        Criterion titleLike =  Restrictions.like("title", "%"+keyword+"%");
        Criterion taglike =  Restrictions.like("tags", "%"+keyword+"%");
        Criterion descriptionLike =  Restrictions.like("description", "%"+keyword+"%");
        c.add(Restrictions.or(titleLike,taglike,descriptionLike));
        
        List<Gig> gigList = c.list();
        
        responseObject.add("gigList", gson.toJsonTree(gigList));
        
        responseObject.addProperty("status", true);
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
    }

   
    

}
