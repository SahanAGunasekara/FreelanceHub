
package hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "userdetails")
public class UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;
    
    @Column(name = "mobile",length = 10,nullable = false)
    private String mobile;
    
    @Column(name = "bio",nullable = false)
    private String bio;
    
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

    
    public String getMobile() {
        return mobile;
    }

    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    
    public String getBio() {
        return bio;
    }

    
    public void setBio(String bio) {
        this.bio = bio;
    }

    
    public Country getCountry() {
        return country;
    }

    
    public void setCountry(Country country) {
        this.country = country;
    }
}
