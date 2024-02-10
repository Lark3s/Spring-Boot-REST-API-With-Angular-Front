package rs.ac.singidunum.isa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.singidunum.isa.models.Article;
import rs.ac.singidunum.isa.models.Comment;
import rs.ac.singidunum.isa.models.User;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> getCommentsByArticle(Article article);

    List<Comment> getCommentsByUser(User user);
}
