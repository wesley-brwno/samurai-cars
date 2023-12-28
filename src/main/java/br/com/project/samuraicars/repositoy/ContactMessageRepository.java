package br.com.project.samuraicars.repositoy;

import br.com.project.samuraicars.model.ContactMessage;
import br.com.project.samuraicars.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    Page<ContactMessage> findAllByUser(User user, Pageable pageable);
}
