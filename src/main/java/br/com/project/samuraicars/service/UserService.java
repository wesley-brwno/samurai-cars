package br.com.project.samuraicars.service;

import br.com.project.samuraicars.exception.BadRequestException;
import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.model.Vehicle;
import br.com.project.samuraicars.repositoy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDetails findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new BadRequestException("User not found"));
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
    }

    public boolean isUserAdmin(UserDetails userDetails) {
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        throw new BadRequestException("The user is not authorized to perform this operation.");
    }

    public boolean isUserOwnerOfResource(UserDetails userDetails, Vehicle vehicle) {
        if (vehicle.getUser().equals(userDetails)) {
            return true;
        }
        throw new BadRequestException("The user is not authorized to perform this operation because the resource does not belong to them.");
    }
}
