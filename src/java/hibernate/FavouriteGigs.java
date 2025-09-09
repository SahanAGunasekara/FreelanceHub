
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
@Table(name = "fav_gigs")
public class FavouriteGigs {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "gigs_id")
    private Gig gig;

    public FavouriteGigs() {
    }

    
    
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

    
    public Gig getGig() {
        return gig;
    }

    
    public void setGig(Gig gig) {
        this.gig = gig;
    }
}
