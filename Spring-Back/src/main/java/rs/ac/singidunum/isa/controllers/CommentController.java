package rs.ac.singidunum.isa.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.singidunum.isa.models.Article;
import rs.ac.singidunum.isa.models.Comment;
import rs.ac.singidunum.isa.models.User;
import rs.ac.singidunum.isa.payload.request.CommentRequest;
import rs.ac.singidunum.isa.payload.response.CommentResponse;
import rs.ac.singidunum.isa.payload.response.MessageResponse;
import rs.ac.singidunum.isa.repository.ArticleRepository;
import rs.ac.singidunum.isa.repository.CommentRepository;
import rs.ac.singidunum.isa.repository.UserRepository;



@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/comments")
    public ResponseEntity<?> createComment(@RequestBody CommentRequest commentRequest) {
        try {
            Comment comment = new Comment();
            Article article1 = articleRepository.getById(commentRequest.getArticle_id());
            List<Article> article = new ArrayList<>();
            User user = userRepository.getById(commentRequest.getUser_id());
            comment.setText(commentRequest.getText());

            if (user == null){
                return ResponseEntity.badRequest().body(new MessageResponse("Error: User is null"));            } else {
                comment.setUser(user);
            }

            article.add(article1);

            if (article == null){
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Article is null"));
            } else {
                comment.setArticle(article1);
            }

            commentRepository.save(comment);

            return ResponseEntity.ok(new MessageResponse("Commented successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Unsuccessfull comment!"));
        }
    }

    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getAllComments(){
        try {
            List<Comment> comments = new ArrayList<Comment>();
            commentRepository.findAll().forEach(comments::add);
            return new ResponseEntity<>(comments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/comments/article/{article_id}")
    public ResponseEntity<List<CommentResponse>> getCommentByArticle(@PathVariable("article_id") long id) {

        try {
            Article article = articleRepository.getById(id);
            List<Comment> comments = commentRepository.getCommentsByArticle(article);
            if (comments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<CommentResponse> commentResponses = new ArrayList<>();
            for (Comment comment: comments){
                CommentResponse temp = new CommentResponse();
                temp.setId(comment.getId());
                temp.setArticle_id(comment.getArticle().getId());
                temp.setText(comment.getText());
                temp.setUsername(comment.getUser().getUsername());
                commentResponses.add(temp);
            }
            return new ResponseEntity<>(commentResponses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/comments/user/{user_id}")
    public ResponseEntity<List<Comment>> getCommentByUser(@PathVariable("user_id") Long id) {

        try {
            User user = userRepository.getById(id);
            List<Comment> comments = commentRepository.getCommentsByUser(user);
            if (comments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(comments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable("id") long id, @RequestBody Comment comment){
        Optional<Comment> commentData = commentRepository.findById(id);
        if (commentData.isPresent()) {
            Comment _comment = commentData.get();
            _comment.setText(comment.getText());
            return new ResponseEntity<>(commentRepository.save(_comment), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Comment> deleteComment(@PathVariable("id") long id) {
        try {
            commentRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
