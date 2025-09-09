
package hibernate;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "chat")
public class Chat {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "sender")
    private User userSender;
    
    @ManyToOne
    @JoinColumn(name = "reciever")
    private User userReciever;
    
    @Column(name = "message",nullable = false)
    private String message;
    
    @Column(name = "created_at")
    private Timestamp time;
    
    @ManyToOne
    @JoinColumn(name = "gigs_id")
    private Gig gig;

    
    public int getId() {
        return id;
    }

    
    public void setId(int id) {
        this.id = id;
    }

    
    public User getUserSender() {
        return userSender;
    }

    
    public void setUserSender(User userSender) {
        this.userSender = userSender;
    }

    
    public User getUserReciever() {
        return userReciever;
    }

    
    public void setUserReciever(User userReciever) {
        this.userReciever = userReciever;
    }

    
    public String getMessage() {
        return message;
    }

   
    public void setMessage(String message) {
        this.message = message;
    }

    
    public Timestamp getTime() {
        return time;
    }

    
    public void setTime(Timestamp time) {
        this.time = time;
    }

    
    public Gig getGig() {
        return gig;
    }

    
    public void setGig(Gig gig) {
        this.gig = gig;
    }
    
}
