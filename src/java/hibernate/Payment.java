
package hibernate;

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
@Table(name = "payment")
public class Payment {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "payment_date",nullable = false)
    private Date paymentDate;
    
    @Column(name = "amount")
    private double amount;
    
    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "gigs_id")
    private Gig gig;
    
    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private Freelancer freelancer;

    public Payment() {
    }

    
    
    public int getId() {
        return id;
    }

    
    public void setId(int id) {
        this.id = id;
    }

    
    public Date getPaymentDate() {
        return paymentDate;
    }

    
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    
    public double getAmount() {
        return amount;
    }

    
    public void setAmount(double amount) {
        this.amount = amount;
    }

    
    public User getUser() {
        return user;
    }

    
    public void setUser(User user) {
        this.user = user;
    }

    
    public Gig getGig() {
        return gig;
    }

    
    public void setGig(Gig gig) {
        this.gig = gig;
    }

    
    public Freelancer getFreelancer() {
        return freelancer;
    }

    
    public void setFreelancer(Freelancer freelancer) {
        this.freelancer = freelancer;
    }
}
