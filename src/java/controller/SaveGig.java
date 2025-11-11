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
import java.io.Serializable;
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
@WebServlet(name = "saveGig", urlPatterns = {"/saveGig"})
public class saveGig extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String gigTitle = request.getParameter("title");
        String price = request.getParameter("price");
        String deliveryTime = request.getParameter("delivery");
        String categoryId = request.getParameter("categoryId");
        String description = request.getParameter("description");
        String[] tags = request.getParameterValues("tags");
        Part Image1 = request.getPart("image1");
        Part Image2 = request.getPart("image2");
        Part Image3 = request.getPart("image3");

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (gigTitle.isEmpty()) {
            responseObject.addProperty("message", "Please enter Gig Title");
        } else if (price.isEmpty()) {
            responseObject.addProperty("message", "please enter price");
        } else if (deliveryTime.isEmpty()) {
            responseObject.addProperty("message", "please select Delivery Time");
        } else if (!Util.isInteger(categoryId)) {
            responseObject.addProperty("message", "please select valid Gig Category");
        } else if (description.isEmpty()) {
            responseObject.addProperty("message", "please enter gig description");
        } else if (tags.length <= 0) {
            responseObject.addProperty("message", "please add tags to your Gig");
        } else if (Image1.getSubmittedFileName() == null) {
            responseObject.addProperty("message", "please add Image 1");
        } else if (Image2.getSubmittedFileName() == null) {
            responseObject.addProperty("message", "please add Image 2");
        } else if (Image3.getSubmittedFileName() == null) {
            responseObject.addProperty("message", "please add Image 3");
        } else {
            Session s = HibernateUtil.getSessionFactory().openSession();
            Category category = (Category) s.load(Category.class, Integer.parseInt(categoryId));

            if (category == null) {
                responseObject.addProperty("message", "please select category");
            } else {
                User u = (User) request.getSession().getAttribute("user");

                if (u != null) {
                    Criteria c = s.createCriteria(Freelancer.class);
                    c.add(Restrictions.eq("user", u));
                    Freelancer freelancer = (Freelancer) c.uniqueResult();

                    GigStatus status = (GigStatus) s.load(GigStatus.class, 1);

                    String mainTag = "";

                    for (String gigTag : tags) {

                        mainTag += gigTag+",";
                    }

                    Gig gig = new Gig();
                    gig.setTitle(gigTitle);
                    gig.setPrice(Integer.parseInt(price));
                    gig.setDescription(description);
                    gig.setTags(mainTag);
                    gig.setDelivery_time(Integer.parseInt(deliveryTime));
                    gig.setFreelancer(freelancer);
                    gig.setCategory(category);
                    gig.setStatus(status);

                    int id = (int) s.save(gig);
                    s.beginTransaction().commit();
                    s.close();

                    String appPath = getServletContext().getRealPath("");
                    String newPath = appPath.replace("build\\web", "web\\Gig-Images");

                    File f = new File(newPath, String.valueOf(id));
                    f.mkdir();

                    File file1 = new File(f, "image1.png");
                    Files.copy(Image1.getInputStream(), file1.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    File file2 = new File(f, "image2.png");
                    Files.copy(Image2.getInputStream(), file2.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    File file3 = new File(f, "image3.png");
                    Files.copy(Image3.getInputStream(), file3.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    responseObject.addProperty("status", true);

                } else {
                    responseObject.addProperty("message", "please Login again");
                }

            }
        }
        
        Gson gson = new Gson();
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);

    }

}
