package br.com.project.samuraicars.service;

import br.com.project.samuraicars.DTO.authentication.UserAuthorizationRequestBody;
import br.com.project.samuraicars.DTO.authentication.UserRegisterPostRequestBody;
import br.com.project.samuraicars.configuration.security.TokenService;
import br.com.project.samuraicars.exception.EmailAlreadyExistException;
import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.repositoy.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private User user;

    private UserRegisterPostRequestBody registerRequestBody;
    private UserAuthorizationRequestBody authorizationRequestBody;

    @BeforeEach
    void setUp() {
        user = createUser();
        registerRequestBody = creatUserRegisterPostRequestBody();
        authorizationRequestBody = createUserAuthorizationRequestBody();
    }

    @Test
    @DisplayName("register should persist User when successful")
    void register_ShouldPersistUser_WhenSuccessful() {
        BDDMockito.when(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.empty());
        BDDMockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

        Assertions.assertThatCode(() -> authenticationService.register(registerRequestBody))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("register should throw EmailAlreadyExistException when an email is already taken")
    void register_ShouldThrowEmailAlreadyExistException_WhenEmailsIsTaken() {
        BDDMockito.when(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.ofNullable(user));
        BDDMockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(null);

        Assertions.assertThatCode(() -> authenticationService.register(registerRequestBody))
                .isInstanceOf(EmailAlreadyExistException.class)
                .hasMessage("Email already exist");
    }

    @Test
    void authorize() {

    }


    private User createUser() {
        return User.builder()
                .id(1L)
                .name("Tom")
                .email("tom@email.com")
                .authorities("USER")
                .build();
    }

    private UserRegisterPostRequestBody creatUserRegisterPostRequestBody() {
        return new UserRegisterPostRequestBody(
                "Tom",
                "tom@email.com",
                "T0mTh3C4t"
        );
    }

    private UserAuthorizationRequestBody createUserAuthorizationRequestBody() {
        return new UserAuthorizationRequestBody(
                "tom@email.com",
                "T0mTh3C4t"
        );
    }
}