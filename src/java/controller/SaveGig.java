package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Category;
import hibernate.Freelancer;
import hibernate.Gig;
import hibernate.GigStatus;
import hibernate.HibernateUtil;
import hibernate.User;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

@MultipartConfig
@WebServlet(name = "SaveGig", urlPatterns = {"/SaveGig"})
public class SaveGig extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("gigTitle");
        String category = request.getParameter("gigCategory");
        String tag = request.getParameter("gigTag");
        String description = request.getParameter("gigDescription");
        String price = request.getParameter("gigPrice");
        String delivery = request.getParameter("gigDelivery");

        Part img1 = request.getPart("image1");
        Part img2 = request.getPart("image2");
        Part img3 = request.getPart("image3");

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (title.isEmpty()) {
            responseObject.addProperty("message", "Gig Title Cannot be empty");
        } else if (!Util.isInteger(category)) {
            responseObject.addProperty("message", "Please select valid Category");
        } else if (tag.isEmpty()) {
            responseObject.addProperty("message", "Please Enter tags");
        } else if (description.isEmpty()) {
            responseObject.addProperty("message", "Please Enter Description");
        } else if (!Util.isDouble(price)) {
            responseObject.addProperty("message", "Please Enter Valid price");
        } else if (!Util.isInteger(delivery)) {
            responseObject.addProperty("message", "Please Enter Valid delivery time");
        } else if (img1.getSubmittedFileName() == null) {
            responseObject.addProperty("message", "Please Add image 1");
        } else if (img2.getSubmittedFileName() == null) {
            responseObject.addProperty("message", "Please Add image 2");
        } else if (img3.getSubmittedFileName() == null) {
            responseObject.addProperty("message", "Please Add image 3");
        } else {
            Category categ = (Category) s.load(Category.class, Integer.valueOf(category));
            if (categ == null) {
                responseObject.addProperty("message", "Please select category from the list");
            } else {
                HttpSession ses = request.getSession();
                User u = (User) ses.getAttribute("user");
                if (u == null) {
                    responseObject.addProperty("message", "Please SignIn");
                } else {
                    Criteria c = s.createCriteria(Freelancer.class);
                    c.add(Restrictions.eq("user", u));
                    Freelancer freelancer = (Freelancer) c.list().get(0);

                    if (freelancer == null) {
                        responseObject.addProperty("message", "Please register as freelancer first");
                    } else {
                        if (!freelancer.getAvailability().equals("Active")) {
                            responseObject.addProperty("message", "Still you have not approved freelancer");
                        } else {
                            GigStatus status = (GigStatus) s.load(GigStatus.class, 1);

                            Gig gig = new Gig();
                            gig.setTitle(title);
                            gig.setPrice(Double.parseDouble(price));
                            gig.setDescription(description);
                            gig.setTags(tag);
                            gig.setDelivery_time(Integer.parseInt(delivery));
                            gig.setFreelancer(freelancer);
                            gig.setCategory(categ);
                            gig.setStatus(status);

                            int id = (int) s.save(gig);
                            s.beginTransaction().commit();
                            s.close();

                            String appPath = getServletContext().getRealPath("");
                            String newPath = appPath.replace("build\\web", "web\\Gig-Images");

                            File f = new File(newPath, String.valueOf(id));
                            f.mkdir();

                            File file1 = new File(f, "image1.png");
                            Files.copy(img1.getInputStream(), file1.toPath(), StandardCopyOption.REPLACE_EXISTING);

                            File file2 = new File(f, "image2.png");
                            Files.copy(img2.getInputStream(), file2.toPath(), StandardCopyOption.REPLACE_EXISTING);

                            File file3 = new File(f, "image3.png");
                            Files.copy(img3.getInputStream(), file3.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            
                            responseObject.addProperty("status", true);
                        }
                    }
                }
            }
        }
        
        Gson gson = new Gson();
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);

    }

}
