package br.com.project.samuraicars.service;

import br.com.project.samuraicars.DTO.user.UserPostRequestBody;
import br.com.project.samuraicars.DTO.user.UserResponseBody;
import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.repositoy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponseBody save(UserPostRequestBody data) {
        User user = this.userRepository.save(new User(data));
        return new UserResponseBody(user);
    }
}
