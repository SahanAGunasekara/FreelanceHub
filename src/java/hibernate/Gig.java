
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
@Table(name = "gigs")
public class Gig implements Serializable{
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "title",nullable = false)
    private String title;
    
    @Column(name = "price",nullable = false)
    private double price;
    
    @Column(name = "description",nullable = false)
    private String description;
    
    @Column(name = "tags",nullable = false)
    private String tags;
    
    @Column(name = "delivery_time")
    private int delivery_time;
    
    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private Freelancer freelancer;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
    @ManyToOne
    @JoinColumn(name = "gig_status_id")
    private GigStatus status;

    
    public int getId() {
        return id;
    }

    
    public void setId(int id) {
        this.id = id;
    }

    
    public String getTitle() {
        return title;
    }

    
    public void setTitle(String title) {
        this.title = title;
    }

    
    public double getPrice() {
        return price;
    }

    
    public void setPrice(double price) {
        this.price = price;
    }

    
    public String getDescription() {
        return description;
    }

    
    public void setDescription(String description) {
        this.description = description;
    }

    
    public String getTags() {
        return tags;
    }

    
    public void setTags(String tags) {
        this.tags = tags;
    }

    
    public int getDelivery_time() {
        return delivery_time;
    }

    
    public void setDelivery_time(int delivery_time) {
        this.delivery_time = delivery_time;
    }

    
    public Freelancer getFreelancer() {
        return freelancer;
    }

    
    public void setFreelancer(Freelancer freelancer) {
        this.freelancer = freelancer;
    }

    
    public Category getCategory() {
        return category;
    }

    
    public void setCategory(Category category) {
        this.category = category;
    }

    
    public GigStatus getStatus() {
        return status;
    }

    
    public void setStatus(GigStatus status) {
        this.status = status;
    }
}
