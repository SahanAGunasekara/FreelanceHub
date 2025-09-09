
package hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "email",length = 50,nullable = false)
    private String email;
    
    @Column(name = "password",length = 45,nullable = false)
    private String password;
    
    @Column(name = "mobile",length = 10,nullable = false)
    private String mobile;
    
    @Column(name = "verification",length = 15,nullable = true)
    private String verification;

    public Admin() {
    }

    
    
    public int getId() {
        return id;
    }

    
    public void setId(int id) {
        this.id = id;
    }

    
    public String getEmail() {
        return email;
    }

    
    public void setEmail(String email) {
        this.email = email;
    }

    
    public String getPassword() {
        return password;
    }

    
    public void setPassword(String password) {
        this.password = password;
    }

    
    public String getMobile() {
        return mobile;
    }

    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    
    
    public String getVerification() {
        return verification;
    }

    
    public void setVerification(String verification) {
        this.verification = verification;
    }
}
