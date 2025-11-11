
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
@Table(name = "qualification")
public class qualification {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "qualification")
    private String qualification;
    
    @ManyToOne
    @JoinColumn(name = "level_id")
    private qualifyLevel level;
    
    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private Freelancer freelancer;

    
    public int getId() {
        return id;
    }

    
    public void setId(int id) {
        this.id = id;
    }

    
    public String getQualification() {
        return qualification;
    }

    
    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    
    public qualifyLevel getLevel() {
        return level;
    }

    
    public void setLevel(qualifyLevel level) {
        this.level = level;
    }

    
    public Freelancer getFreelancer() {
        return freelancer;
    }

    
    public void setFreelancer(Freelancer freelancer) {
        this.freelancer = freelancer;
    }
}
