package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.AdvanceDetails;
import hibernate.Country;
import hibernate.HibernateUtil;
import hibernate.User;
import hibernate.UserDetails;
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
@WebServlet(name = "updateUser", urlPatterns = {"/updateUser"})
public class updateUser extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //System.out.println("Request recieved");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        String countryId = request.getParameter("country");
        String bio = request.getParameter("bio");

        Part profileImg = request.getPart("image");

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (username.isEmpty()) {
            responseObject.addProperty("message", "username cannot be empty");
        } else if (email.isEmpty()) {
            responseObject.addProperty("message", "Email cannot be empty");
        } else if (!Util.isEmailValid(email)) {
            responseObject.addProperty("message", "Please enter valid email");
        } else if (mobile.isEmpty()) {
            responseObject.addProperty("message", "Please enter mobile");
        } else if (Integer.valueOf(countryId) == 0) {
            responseObject.addProperty("message", "Please Select country");
        } else if (bio.isEmpty()) {
            responseObject.addProperty("message", "Please add bio");
        } else {

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            HttpSession ses = request.getSession();
            User u = (User) ses.getAttribute("user");

            Criteria c = s.createCriteria(User.class);
            c.add(Restrictions.eq("email", u.getEmail()));

            if (!c.list().isEmpty()) {
                User user = (User) c.uniqueResult();
                user.setUsername(username);
                user.setEmail(email);

                s.update(user);

                Criteria c1 = s.createCriteria(UserDetails.class);
                c1.add(Restrictions.eq("user", user));

                if (c1.list().isEmpty()) {

                    Country cntry = (Country) s.get(Country.class, Integer.valueOf(countryId));

                    UserDetails ud = new UserDetails();
                    ud.setUser(user);
                    ud.setMobile(mobile);
                    ud.setBio(bio);
                    ud.setCountry(cntry);

                    s.save(ud);

                }else{
                    
                    Country cntry = (Country) s.get(Country.class, Integer.valueOf(countryId));
                    
                    UserDetails usd = (UserDetails)c1.uniqueResult();
                    usd.setUser(user);
                    usd.setMobile(mobile);
                    usd.setBio(bio);
                    usd.setCountry(cntry);
                    
                    s.update(usd);
                
                }

                s.beginTransaction().commit();
                ses.setAttribute("user", user);

                if (profileImg.getSubmittedFileName() != null) {
                    String appPath = getServletContext().getRealPath("");//full path of the web pages folder
                    String newPath = appPath.replace("build\\web", "web\\profile-images");

                    File f = new File(newPath, String.valueOf(user.getId()));
                    f.mkdir();

                    File file1 = new File(f, "image1.png");
                    Files.copy(profileImg.getInputStream(), file1.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }

                responseObject.addProperty("status", true);

            } else {
                responseObject.addProperty("message", "Error Please login again");
            }

        }

        Gson gson = new Gson();
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);

    }

}
