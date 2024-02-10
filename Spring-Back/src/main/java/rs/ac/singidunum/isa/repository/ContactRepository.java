package rs.ac.singidunum.isa.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.singidunum.isa.models.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findByTitleContaining(String title);
}
