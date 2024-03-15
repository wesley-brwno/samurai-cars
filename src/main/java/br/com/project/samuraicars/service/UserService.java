package br.com.project.samuraicars.service;

import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.model.Vehicle;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    User findById(Long userId);
    boolean isUserAdmin(UserDetails userDetails);
    boolean isUserOwnerOfResource(UserDetails userDetails, Vehicle vehicle);
}
