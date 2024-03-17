package br.com.project.samuraicars.service;

import br.com.project.samuraicars.DTO.authentication.UserAuthorizationRequestBody;
import br.com.project.samuraicars.DTO.authentication.UserRegisterPostRequestBody;
import br.com.project.samuraicars.configuration.security.TokenService;
import br.com.project.samuraicars.exception.EmailAlreadyExistException;
import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.repositoy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Transactional
    public String register(UserRegisterPostRequestBody user) {
        if (userRepository.findByEmail(user.email()).isPresent()) {
            throw new EmailAlreadyExistException("Email already exists");
        }
        String encode = new BCryptPasswordEncoder().encode(user.password());
        userRepository.save(new User(encode, user));
        return authorize(new UserAuthorizationRequestBody(user.email(), user.password()));
    }

    public String authorize(UserAuthorizationRequestBody user) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.email(), user.password());
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            return tokenService.generateToken((User) authenticate.getPrincipal());
        } catch (AuthenticationException e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }
}
