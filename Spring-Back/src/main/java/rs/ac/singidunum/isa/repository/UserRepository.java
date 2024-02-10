package rs.ac.singidunum.isa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.singidunum.isa.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  List<User> searchByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
}
