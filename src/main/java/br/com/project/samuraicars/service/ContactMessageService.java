package br.com.project.samuraicars.service;

import br.com.project.samuraicars.DTO.contactMessage.ContactMessageRequestBody;
import br.com.project.samuraicars.DTO.contactMessage.ContactMessageResponseBody;
import br.com.project.samuraicars.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

public interface ContactMessageService {
    void save(ContactMessageRequestBody requestBody);
    void delete(Long id, UserDetails userDetails);
    ContactMessageResponseBody listById(Long id, UserDetails userDetails);
    Page<ContactMessageResponseBody> findByUserPageable(Pageable pageable, User user);
}
