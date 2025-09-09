package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import model.Mail;
import model.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@WebServlet(name = "changePasswordCode", urlPatterns = {"/changePasswordCode"})
public class changePasswordCode extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //System.out.println("code send");

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        String gCode = Util.generateCode();

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        HttpSession ses = request.getSession();
        User u = (User) ses.getAttribute("user");

        if (u != null) {

            String body = "<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "<head>\n"
                    + "  <meta charset=\"UTF-8\">\n"
                    + "  <style>\n"
                    + "    body {\n"
                    + "      font-family: Arial, sans-serif;\n"
                    + "      background-color: #f4f4f4;\n"
                    + "      margin: 0;\n"
                    + "      padding: 0;\n"
                    + "    }\n"
                    + "    .container {\n"
                    + "      background-color: #ffffff;\n"
                    + "      max-width: 500px;\n"
                    + "      margin: 30px auto;\n"
                    + "      padding: 20px;\n"
                    + "      border-radius: 8px;\n"
                    + "      box-shadow: 0px 2px 8px rgba(0,0,0,0.1);\n"
                    + "    }\n"
                    + "    h2 {\n"
                    + "      color: #333333;\n"
                    + "      text-align: center;\n"
                    + "    }\n"
                    + "    .code-box {\n"
                    + "      background-color: #f0f0f0;\n"
                    + "      padding: 15px;\n"
                    + "      text-align: center;\n"
                    + "      font-size: 24px;\n"
                    + "      font-weight: bold;\n"
                    + "      letter-spacing: 4px;\n"
                    + "      border-radius: 5px;\n"
                    + "      margin: 20px 0;\n"
                    + "      color: #2c3e50;\n"
                    + "    }\n"
                    + "    p {\n"
                    + "      color: #555555;\n"
                    + "      font-size: 14px;\n"
                    + "      line-height: 1.6;\n"
                    + "    }\n"
                    + "    .footer {\n"
                    + "      font-size: 12px;\n"
                    + "      text-align: center;\n"
                    + "      color: #999999;\n"
                    + "      margin-top: 20px;\n"
                    + "    }\n"
                    + "  </style>\n"
                    + "</head>\n"
                    + "<body>\n"
                    + "  <div class=\"container\">\n"
                    + "    <h2>Password Verification Code</h2>\n"
                    + "    <p>Dear User,</p>\n"
                    + "    <p>Please use the following verification code to complete your password update process:</p>\n"
                    + "    <div class=\"code-box\">'"+gCode+"'</div>"
                    + "    <p>If you didn’t request this, please ignore this email.</p>\n"
                    + "    <div class=\"footer\">\n"
                    + "      © '"+java.time.Year.now()+"' FreelanceHub. All rights reserved.\n"
                    + "    </div>\n"
                    + "  </div>\n"
                    + "</body>\n"
                    + "</html>";

            u.setpCode(gCode);

            s.update(u);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Mail.sendMail(u.getEmail(), "Password verification Code", body);
                }
            }).start();
            
            s.beginTransaction().commit();
            
            responseObject.addProperty("status", true);

        } else {
            responseObject.addProperty("message", "Please signIn again");
        }
        
        Gson gson = new Gson();
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);

    }

}
