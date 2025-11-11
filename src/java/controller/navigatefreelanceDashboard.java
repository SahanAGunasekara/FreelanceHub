package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import hibernate.Freelancer;
import hibernate.User;
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "navigatefreelanceDashboard", urlPatterns = {"/navigatefreelanceDashboard"})
public class navigatefreelanceDashboard extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //System.out.println("naviagtion process started");
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");

        if (u != null) {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            Criteria c1 = s.createCriteria(Freelancer.class);
            c1.add(Restrictions.eq("user", u));

            if (!c1.list().isEmpty()) {
                System.out.println("status set to true");
                responseObject.addProperty("status", true);

            } else {
                responseObject.addProperty("message", "Please register as freelancer first");
            }
        } else {
            responseObject.addProperty("message", "Please login again");
        }

        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);

    }

}
