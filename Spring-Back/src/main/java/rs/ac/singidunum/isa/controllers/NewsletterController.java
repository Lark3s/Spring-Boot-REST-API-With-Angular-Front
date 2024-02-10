package rs.ac.singidunum.isa.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import rs.ac.singidunum.isa.models.Contact;
import rs.ac.singidunum.isa.models.Newsletter;
import rs.ac.singidunum.isa.repository.ArticleRepository;
import rs.ac.singidunum.isa.repository.NewsletterRepository;

@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api")
public class NewsletterController {

    @Autowired
    NewsletterRepository newsletterRepository;

    @GetMapping("/newsletter")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Newsletter>> getAllNewsletters(@RequestParam(required = false) String title) {
        try {
            List<Newsletter> newsletters = new ArrayList<Newsletter>();
            if (title == null)
                newsletterRepository.findAll().forEach(newsletters::add);
            if (newsletters.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(newsletters, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/newsletter")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Newsletter> createNewsletter(@RequestBody Newsletter newsletter) {
        try {
            Newsletter _newsletter = newsletterRepository
                    .save(new Newsletter(newsletter.getEmail()));
            return new ResponseEntity<>(_newsletter, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/newsletter/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Newsletter> updateNewsletter(@PathVariable("id") long id, @RequestBody Newsletter newsletter) {
        Optional<Newsletter> newsletterData = newsletterRepository.findById(id);
        if (newsletterData.isPresent()) {
            Newsletter _newsletter = newsletterData.get();
            _newsletter.setEmail(newsletter.getEmail());
            return new ResponseEntity<>(newsletterRepository.save(_newsletter), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/newsletter/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteContact(@PathVariable("id") long id) {
        try {
            newsletterRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/newsletter")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteAllContacts() {
        try {
            newsletterRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
