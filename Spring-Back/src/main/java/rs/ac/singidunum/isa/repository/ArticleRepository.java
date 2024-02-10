package rs.ac.singidunum.isa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.singidunum.isa.models.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByPublished(boolean published);
    List<Article> findByTitleContaining(String title);

    List<Article> findByTitleContainingAndPublished(String title, boolean published);

    Optional<Article> findByIdAndAndPublished(long id, boolean published);
}
