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
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new BadRequestException("User not found"));
    }

    @Override
    public boolean isUserAdmin(UserDetails userDetails) {
        return userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
    }

    @Override

    public boolean isUserOwnerOfResource(UserDetails userDetails, Vehicle vehicle) {
        return vehicle.getUser().equals(userDetails);
    }
}
