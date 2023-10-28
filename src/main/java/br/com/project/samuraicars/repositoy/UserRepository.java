package br.com.project.samuraicars.repositoy;

import br.com.project.samuraicars.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
