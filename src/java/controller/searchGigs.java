package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Category;
import hibernate.Country;
import hibernate.Freelancer;
import hibernate.Gig;
import hibernate.GigStatus;
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
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "searchGigs", urlPatterns = {"/searchGigs"})
public class searchGigs extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        JsonObject requestJson = gson.fromJson(request.getReader(), JsonObject.class);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        Criteria c1 = s.createCriteria(Gig.class);

        if (requestJson.has("gigCategory")) {
            int category = requestJson.get("gigCategory").getAsInt();
            if (category != 0) {
                Category catgry = (Category) s.get(Category.class, category);

                c1.add(Restrictions.eq("category", catgry));
            }

        }

        if (requestJson.has("deliveryTime")) {
            String time = requestJson.get("deliveryTime").getAsString();
            Integer t = Integer.valueOf(time);
            if (t == 24) {
                c1.add(Restrictions.lt("delivery_time", 1));
            } else if (t == 72) {
                c1.add(Restrictions.lt("delivery_time", 3));
            } else if (t == 168) {
                c1.add(Restrictions.lt("delivery_time", 7));
            }

        }

        if (requestJson.has("country")) {
            String country = requestJson.get("country").getAsString();
            int ctry = Integer.parseInt(country);
            if (ctry != 0) {
                Country cname = (Country) s.get(Country.class, ctry);
                Criteria c2 = s.createCriteria(Freelancer.class);

                c2.add(Restrictions.eq("country", cname));
                List<Freelancer> fList = c2.list();
                c1.add(Restrictions.in("freelancer", fList));
            }
        }

        if (requestJson.has("language")) {
            int planguage = requestJson.get("language").getAsInt();
            if (planguage == 1) {
                c1.add(Restrictions.eq("tags", "%JavaScript%"));
            } else if (planguage == 2) {
                c1.add(Restrictions.like("tags", "%Python%"));
            } else if (planguage == 3) {
                c1.add(Restrictions.like("tags", "%Java%"));
            } else if (planguage == 4) {
                c1.add(Restrictions.like("tags", "%PHP%"));
            } else if (planguage == 5) {
                c1.add(Restrictions.like("tags", "%C#%"));
            } else if (planguage == 6) {
                c1.add(Restrictions.like("tags", "%Ruby%"));
            } else {

            }
        }

        if (requestJson.has("availability")) {
            int availability = requestJson.get("availability").getAsInt();
            if (availability != 0) {
                if (availability == 1) {
                    GigStatus gs = (GigStatus) s.get(GigStatus.class, 2);
                    c1.add(Restrictions.eq("status", gs));
                } else if (availability == 2) {
                    GigStatus gs = (GigStatus) s.get(GigStatus.class, 1);
                    c1.add(Restrictions.eq("status", gs));
                }
            }
        }

        if (requestJson.has("rating")) {
            int rate = requestJson.get("rating").getAsInt();
            if (rate != 0) {
                if (rate == 1) {
                    Criteria c3 = s.createCriteria(Freelancer.class);
                    c3.add(Restrictions.eq("rating", "1"));
                    List<Freelancer> frList = c3.list();

                    c1.add(Restrictions.in("freelancer", frList));
                } else if (rate == 2) {
                    Criteria c3 = s.createCriteria(Freelancer.class);
                    c3.add(Restrictions.eq("rating", "2"));
                    List<Freelancer> frList = c3.list();

                    c1.add(Restrictions.in("freelancer", frList));
                }else if(rate == 3){
                    Criteria c3 = s.createCriteria(Freelancer.class);
                    c3.add(Restrictions.eq("rating", "3"));
                    List<Freelancer> frList = c3.list();

                    c1.add(Restrictions.in("freelancer", frList));
                }else if(rate == 4){
                    Criteria c3 = s.createCriteria(Freelancer.class);
                    c3.add(Restrictions.eq("rating", "4"));
                    List<Freelancer> frList = c3.list();

                    c1.add(Restrictions.in("freelancer", frList));
                }else if(rate == 5){
                    Criteria c3 = s.createCriteria(Freelancer.class);
                    c3.add(Restrictions.eq("rating", "5"));
                    List<Freelancer> frList = c3.list();

                    c1.add(Restrictions.in("freelancer", frList));
                }

            }
        }
        
        if (requestJson.has("startingPrice") && requestJson.has("endPriceRange")) {
            double startPrice = requestJson.get("startingPrice").getAsDouble();
            double endPrice = requestJson.get("endPriceRange").getAsDouble();
            
            c1.add(Restrictions.ge("price", startPrice));
            c1.add(Restrictions.le("price", endPrice));
        }

        if (requestJson.has("firstResult")) {
            int firstResult = requestJson.get("firstResult").getAsInt();
            c1.setFirstResult(firstResult);
            c1.setMaxResults(6);
        }

        List<Gig> gigList = c1.list();

        responseObject.add("gigList", gson.toJsonTree(gigList));
        responseObject.addProperty("status", true);
        response.setContentType("application/json");
        String toJson = gson.toJson(responseObject);
        response.getWriter().write(toJson);

    }

}
