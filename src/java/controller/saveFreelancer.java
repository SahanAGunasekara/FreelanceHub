package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import hibernate.AdvanceDetails;
import hibernate.Country;
import hibernate.Freelancer;
import hibernate.HibernateUtil;
import hibernate.User;
import hibernate.qualification;
import hibernate.qualifyLevel;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import model.Qualification;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

@MultipartConfig
@WebServlet(name = "saveFreelancer", urlPatterns = {"/saveFreelancer"})
public class saveFreelancer extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //System.out.println("Request recieved save freelancer");

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        String title = request.getParameter("title");
        String availability = request.getParameter("availability");
        String skills = request.getParameter("skills");
        String linkedInURL = request.getParameter("linkedInURL");
        String portfolioURL = request.getParameter("portfolioURL");
        String countryId = request.getParameter("country");
        String description = request.getParameter("description");
        String qualifications = request.getParameter("qualifications");

        Part pdfPortfolio = request.getPart("portfolioPDF");

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        HttpSession ses = request.getSession();
        User u = (User) ses.getAttribute("user");

        if (u != null) {

            Criteria c1 = s.createCriteria(AdvanceDetails.class);
            c1.add(Restrictions.eq("user", u));

            if (!c1.list().isEmpty()) {
                AdvanceDetails av = (AdvanceDetails) c1.uniqueResult();
                if (av.getVerification().equals("verified")) {
                    //responseObject.addProperty("message", "Good to go");
                    if (title.isEmpty()) {
                        responseObject.addProperty("message", "Please enter title");
                    } else if (availability.equals(null)) {
                        responseObject.addProperty("message", "Please select availability");
                    } else if (skills.isEmpty()) {
                        responseObject.addProperty("message", "Please enter skills");
                    } else if (linkedInURL.isEmpty()) {
                        responseObject.addProperty("message", "Please add linkedIn URL");
                    } else if (portfolioURL.isEmpty()) {
                        responseObject.addProperty("message", "Please add Portfolio URL");
                    } else if (Integer.parseInt(countryId) == 0) {
                        responseObject.addProperty("message", "Please select a country");
                    } else if (description.isEmpty()) {
                        responseObject.addProperty("message", "Please enter freelancer decription");
                    } else if (qualifications.isEmpty()) {
                        responseObject.addProperty("message", "Please add qualifications");
                    } else {

                        Country ctry = (Country) s.get(Country.class, Integer.parseInt(countryId));

                        Freelancer freelancer = new Freelancer();
                        freelancer.setUser(u);
                        freelancer.setTitle(title);
                        freelancer.setAvailability("Ready");
                        freelancer.setLinkedIn(linkedInURL);
                        freelancer.setPortfolio(portfolioURL);
                        freelancer.setDescription(description);
                        freelancer.setCountry(ctry);
                        freelancer.setSkills(skills);

                        int fid = (int) s.save(freelancer);
                        s.beginTransaction().commit();

                        Type listType = new TypeToken<List<Qualification>>() {
                        }.getType();

                        List<Qualification> qualific = gson.fromJson(qualifications, listType);

                        for (Qualification f : qualific) {
//                    System.out.println("Qualification: " + f.getQualification());
//                    System.out.println("Level: " + f.getLevel());
                            Freelancer freel = (Freelancer) s.get(Freelancer.class, fid);
                            Criteria c2 = s.createCriteria(qualifyLevel.class);
                            c2.add(Restrictions.eq("level", f.getLevel()));
                            qualifyLevel level = (qualifyLevel) c2.list().get(0);

                            qualification quali = new qualification();
                            quali.setQualification(f.getQualification());
                            quali.setLevel(level);
                            quali.setFreelancer(freel);
                            s.save(quali);
                            s.beginTransaction().commit();
                        }
                        
                        if (pdfPortfolio.getSubmittedFileName()!=null) {
                            String appPath = getServletContext().getRealPath("");
                            String newPath = appPath.replace("build\\web", "web\\pdf-portfolio");
                            
                            File f = new File(newPath, String.valueOf(u.getId()));
                            f.mkdir();
                            
                            File file1 = new File(f, u.getUsername()+"portfolio.pdf");
                            Files.copy(pdfPortfolio.getInputStream(), file1.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        }
 

                    }

                } else {
                    responseObject.addProperty("message", "Please verify your account first");
                }
            } else {
                responseObject.addProperty("message", "Please verify your account first");
            }

        } else {
            responseObject.addProperty("message", "Please login again");
        }

        responseObject.addProperty("status", true);
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
    }

}
