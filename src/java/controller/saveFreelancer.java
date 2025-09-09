package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.AdvanceDetails;
import hibernate.Country;
import hibernate.Freelancer;
import hibernate.HibernateUtil;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "saveFreelancer", urlPatterns = {"/saveFreelancer"})
public class saveFreelancer extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //System.out.println("Request recieved");

        Gson gson = new Gson();
        JsonObject freelancerData = gson.fromJson(request.getReader(), JsonObject.class);

        String skill = freelancerData.get("skill").getAsString();
        String portUrl = freelancerData.get("portfolioURL").getAsString();
        String linkedInURL = freelancerData.get("linkedInURL").getAsString();
        String description = freelancerData.get("description").getAsString();
        String country = freelancerData.get("gigCountry").getAsString();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        HttpSession ses = request.getSession();
        User u = (User) ses.getAttribute("user");

        if (skill.isEmpty()) {
            responseObject.addProperty("message", "Please add your skill");
        } else if (portUrl.isEmpty()) {
            responseObject.addProperty("message", "Add your Portfolio URL");
        } else if (linkedInURL.isEmpty()) {
            responseObject.addProperty("message", "Add your LinkedIn URL");
        } else if (description.isEmpty()) {
            responseObject.addProperty("message", "Add your Bio");
        }else if(!Util.isInteger(country)){
            responseObject.addProperty("message", "Please select valid country");
        }else if(Integer.parseInt(country) == 0){
            responseObject.addProperty("message", "Please select country");
        } else {

            if (u != null) {
                SessionFactory sf = HibernateUtil.getSessionFactory();
                Session s = sf.openSession();
                
                Country countr = (Country)s.get(Country.class, Integer.parseInt(country));

                Criteria c1 = s.createCriteria(AdvanceDetails.class);
                c1.add(Restrictions.eq("user", u));
                AdvanceDetails adv = (AdvanceDetails) c1.list().get(0);

                if (adv.getVerification().equals("verified")) {
                Freelancer fr = new Freelancer();
                fr.setUser(u);
                fr.setSkill(skill);
                fr.setAvailability("Pending");
                fr.setLinkedIn(linkedInURL);
                fr.setPortfolio(portUrl);
                fr.setDescription(description);
                fr.setCountry(countr);

                s.save(fr);
                s.beginTransaction().commit();
                    responseObject.addProperty("status", true);
                    responseObject.addProperty("message", "Successfully Applied as Freelancer");
                } else {
                    responseObject.addProperty("message", "please verify your account");
                    //System.out.println("please verify your account");
                }

            } else {
                responseObject.addProperty("message", "please signIn");
                //System.out.println("please signIn");
            }

        }

        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);

    }

}
