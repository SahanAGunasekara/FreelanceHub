
package controller;

import com.google.gson.Gson;
import hibernate.HibernateUtil;

import hibernate.Rtype;
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


@WebServlet(name = "addRole", urlPatterns = {"/addRole"})
public class addRole extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        
        Criteria c = s.createCriteria(Rtype.class);
        List<Rtype> rlist = c.list();
        
//        for (Rtype r : rlist) {
//            System.out.println(r.getId());
//            System.out.println(r.getName());
//        }
        
        Gson gson = new Gson();
        String toJson = gson.toJson(rlist);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
        
        
    }

    

}
