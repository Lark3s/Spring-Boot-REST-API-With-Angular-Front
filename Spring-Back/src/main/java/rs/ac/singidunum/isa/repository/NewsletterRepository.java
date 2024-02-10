package rs.ac.singidunum.isa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.singidunum.isa.models.Article;
import rs.ac.singidunum.isa.models.Comment;
import rs.ac.singidunum.isa.models.Newsletter;

import java.util.List;
import java.util.Optional;

public interface NewsletterRepository extends JpaRepository<Newsletter, Long> {

//    Optional<Comment> findByArticle(Article article);
//
//    List<Comment> searchByArticle(Article article);
//
//    Boolean existsByArticle(Article article);

}
