package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Freelancer;
import hibernate.Gig;
import hibernate.HibernateUtil;
import hibernate.Payment;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.PayHere;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@WebServlet(name = "Checkout", urlPatterns = {"/Checkout"})
public class Checkout extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        JsonObject requestObject = gson.fromJson(request.getReader(), JsonObject.class);

        double price = requestObject.get("gigprice").getAsDouble();
        String title = requestObject.get("gigTitle").getAsString();
        String gigId = requestObject.get("gigId").getAsString();

        HttpSession ses = request.getSession();
        User u = (User) ses.getAttribute("user");
        

        if (u != null) {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            Transaction tr = s.beginTransaction();
            
            Gig gig = (Gig) s.get(Gig.class, Integer.parseInt(gigId));
            Payment payment = new Payment();
            payment.setPaymentDate(new Date());
            payment.setAmount(price);
            payment.setUser(u);
            payment.setGig(gig);
            payment.setFreelancer(gig.getFreelancer());

            int paymentId = (int) s.save(payment);
            tr.commit();

            processCheckout(paymentId, title, price,responseObject,u,tr);

            responseObject.addProperty("status", true);
        } else {
            responseObject.addProperty("message", "Please signIn");
        }

        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
    }

    private void processCheckout(int paymentId, String title, double price,JsonObject responseObject,User u,Transaction tr) {

        try {
            
            String merahantID = "1224086";
        String merchantSecret = "MTk2NzQxODk3NTI0ODExMDM1MDQxNjcwMDI4MDAxMjM2Njg3OTI2MQ==";
        String orderID = "#000" + paymentId;
        String currency = "LKR";
        String formattedAmount = new DecimalFormat("0.00").format(price);
        String merchantSecretMD5 = PayHere.generateMd5(merchantSecret);
        
        String hash = PayHere.generateMd5(merahantID+orderID+formattedAmount+currency+merchantSecretMD5);
        
        JsonObject payHereJson = new JsonObject();
            payHereJson.addProperty("sandbox", true);
            payHereJson.addProperty("merchant_id", merahantID);
            payHereJson.addProperty("return_url", "");
            payHereJson.addProperty("cancel_url", "");
            payHereJson.addProperty("notify_url", " https://de161c12e2c6.ngrok-free.app/FreelanceHub/VerifyPayment");
            
            payHereJson.addProperty("order_id", orderID);
            payHereJson.addProperty("items", title);
            payHereJson.addProperty("amount", formattedAmount);
            payHereJson.addProperty("currency", currency);
            
            payHereJson.addProperty("hash", hash);
            payHereJson.addProperty("first_name", u.getUsername());
            payHereJson.addProperty("last_name", " ");
            payHereJson.addProperty("email", u.getEmail());
            payHereJson.addProperty("phone", "0771234567");
            payHereJson.addProperty("address", "");
            payHereJson.addProperty("city", "");
            payHereJson.addProperty("country", "Sri Lanka");
            
            responseObject.addProperty("status", true);
            responseObject.addProperty("message", "Checkout completed");
            responseObject.add("payhereJson", new Gson().toJsonTree(payHereJson));
            
        } catch (Exception e) {
            tr.rollback();
        }
        

    }

}
