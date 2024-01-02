package br.com.project.samuraicars.repositoy;

import br.com.project.samuraicars.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for User Respository")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Test
    @DisplayName("Save persists user when successful")
    void save_PersistUser_WhenSuccessful() {
        User userTobeSaved = createUser();
        User savedUser = this.userRepository.save(userTobeSaved);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isNotNull();
        Assertions.assertThat(savedUser.getName()).isEqualTo(userTobeSaved.getName());
    }

    @Test
    @DisplayName("Save updates user when successful")
    void save_UpdatesUser_WhenSuccessful() {
        User userTobeSaved = createUser();
        User savedUser = this.userRepository.save(userTobeSaved);

        savedUser.setName("Updated username");
        User updatedUser = this.userRepository.save(savedUser);

        Assertions.assertThat(updatedUser).isNotNull();
        Assertions.assertThat(updatedUser.getId()).isNotNull();
        Assertions.assertThat((updatedUser.getName())).isEqualTo(savedUser.getName());
    }

    @Test
    @DisplayName("Delete removes user when successful")
    void delete_removesUser_WhenSuccessful() {
        User userTobeSaved = createUser();
        User savedUser = this.userRepository.save(userTobeSaved);

        this.userRepository.delete(savedUser);

        Optional<User> userOptional = this.userRepository.findById(savedUser.getId());

        Assertions.assertThat(userOptional).isEmpty();
    }

    @Test
    @DisplayName("Find User By Email return an optional of user when successful")
    void findUserByEmail_returnUserOptional_WhenSuccessful() {
        User userTobeSaved = createUser();
        User savedUser = this.userRepository.save(userTobeSaved);

        Optional<User> userOptional = this.userRepository.findUserByEmail(userTobeSaved.getEmail());

        Assertions.assertThat(userOptional).isNotEmpty();
        Assertions.assertThat(userOptional.get()).isEqualTo(savedUser);
    }

    @Test
    @DisplayName("Find User By Email returns empty optional when user is not found")
    void findUserByEmail_returnEmptyOptional_WhenUserIsNotFound() {
        Optional<User> userOptional = this.userRepository.findUserByEmail("----");
        
        Assertions.assertThat(userOptional).isEmpty();
    }

    @Test
    @DisplayName("Find By Email return an optional of UserDetails when successful")
    void findByEmail_returnUserDetailsOptional_WhenSuccessful() {
        User userTobeSaved = createUser();
        this.userRepository.save(userTobeSaved);

        Optional<UserDetails> userDetailsOptional = this.userRepository.findByEmail(userTobeSaved.getEmail());

        Assertions.assertThat(userDetailsOptional).isNotEmpty();
        Assertions.assertThat(userDetailsOptional.get()).isEqualTo(userTobeSaved);
    }

    @Test
    @DisplayName("Find By Email returns empty optional when UserDetails is not found")
    void findByEmail_returnEmptyOptional_WhenUserDetailsIsNotFound() {
        Optional<UserDetails> userOptional = this.userRepository.findByEmail("-------");

        Assertions.assertThat(userOptional).isEmpty();
    }

    private User createUser() {
        return User.builder()
                .name("Samurai Cars User")
                .email("samuraiUser@email.com")
                .password("$2y$10$oa3BbtxMCC1NR3u9CNbCielWGtpeck3zsVc6FLtxs05ux99V7lPCS") // samuraiUser
                .authorities("USER")
                .build();
    }
}