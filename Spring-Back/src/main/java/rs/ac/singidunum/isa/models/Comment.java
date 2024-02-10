package rs.ac.singidunum.isa.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @ManyToOne(optional = true)
    @JoinColumn(name="articles", referencedColumnName = "id")
    private Article article;

    @Column(name = "text")
    private String text;

    @OneToOne(optional = true)
    @JoinColumn(name = "users", referencedColumnName = "id")
    @JsonBackReference
    private User user;

    public Comment() {}

    public Comment(String text){
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
