package rs.ac.singidunum.isa.models;

import javax.persistence.*;

@Entity
@Table(name = "newsletter")
public class Newsletter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "email")
    private String email;

    public Newsletter() {

    }

    public Newsletter(String email){
        this.email = email;
    }

    public String toString() {
        return "Newsletter [id=" + id + ", email=" + email + "]";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
