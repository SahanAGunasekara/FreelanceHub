
package hibernate;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "advance_details")
public class AdvanceDetails implements Serializable{
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "verification",length = 45,nullable = false)
    private String verification;
    
    @Column(name = "joined_at",nullable = false)
    private Date joined;
    
    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    public AdvanceDetails() {
    }

    
    
    public int getId() {
        return id;
    }

    
    public void setId(int id) {
        this.id = id;
    }

    
    public String getVerification() {
        return verification;
    }

    
    public void setVerification(String verification) {
        this.verification = verification;
    }

    
    public Date getJoined() {
        return joined;
    }

    
    public void setJoined(Date joined) {
        this.joined = joined;
    }

    
    public User getUser() {
        return user;
    }

    
    public void setUser(User user) {
        this.user = user;
    }
}
