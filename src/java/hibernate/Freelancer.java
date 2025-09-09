
package hibernate;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "freelancer")
public class Freelancer implements Serializable{
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;
    
    @Column(name = "skills",length =50,nullable = false )
    private String skill;
    
    @Column(name = "availability",length = 45,nullable = false)
    private String availability;
    
    @Column(name = "rating",length = 45,nullable = true)
    private String rating;
    
    @Column(name = "linkedIn_url",length = 200,nullable = false)
    private String linkedIn;
    
    @Column(name = "portfolio_url",length = 200,nullable = false)
    private String portfolio;
    
    @Column(name = "description",nullable = false)
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    
    public int getId() {
        return id;
    }

    
    public void setId(int id) {
        this.id = id;
    }

    
    public User getUser() {
        return user;
    }

    
    public void setUser(User user) {
        this.user = user;
    }

    
    public String getSkill() {
        return skill;
    }

    
    public void setSkill(String skill) {
        this.skill = skill;
    }

    
    public String getAvailability() {
        return availability;
    }

    
    public void setAvailability(String availability) {
        this.availability = availability;
    }

    
    public String getRating() {
        return rating;
    }

    
    public void setRating(String rating) {
        this.rating = rating;
    }

    
    public String getLinkedIn() {
        return linkedIn;
    }

    
    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }

    
    public String getPortfolio() {
        return portfolio;
    }

    
    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }

    
    public String getDescription() {
        return description;
    }

    
    public void setDescription(String description) {
        this.description = description;
    }

    
    public Country getCountry() {
        return country;
    }

    
    public void setCountry(Country country) {
        this.country = country;
    }
    
}
