package br.com.project.samuraicars.repositoy;

import br.com.project.samuraicars.model.ContactMessage;
import br.com.project.samuraicars.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

@DataJpaTest
class ContactMessageRepositoryTest {

    @Autowired
    private ContactMessageRepository contactMessageRepository;
    @Autowired
    private UserRepository userRepository;

    private ContactMessage contactMessage;
    private User user;
    @BeforeEach
    void setUp() {
        contactMessage = createValidContactMessage();
        user = createValidUser();
    }

    @Test
    @DisplayName("save should persist ContactMessage when successful")
    void save_ShouldPersistContactMessage_WhenSuccessful() {
        contactMessage.setId(null);

        ContactMessage savedMessage = contactMessageRepository.save(contactMessage);

        Assertions.assertThat(savedMessage).isNotNull();
        Assertions.assertThat(savedMessage.getId())
                .isNotNull()
                .isGreaterThan(0);
        Assertions.assertThat(savedMessage.getMessage())
                .isEqualTo(contactMessage.getMessage());
    }

    @Test
    @DisplayName("save should update ContactMessage when successful")
    void save_ShouldUpdateContacMessage_WhenSuccessful() {
        contactMessage.setId(null);
        ContactMessage savedMessage = contactMessageRepository.save(contactMessage);
        String newName = "new name";
        String newMessage = "Some new text...";
        savedMessage.setMessage(newMessage);
        savedMessage.setName(newName);

        ContactMessage updatedMessage = contactMessageRepository.save(savedMessage);

        Assertions.assertThat(updatedMessage).isNotNull();
        Assertions.assertThat(updatedMessage.getId())
                .isGreaterThan(0)
                .isEqualTo(savedMessage.getId());
        Assertions.assertThat(updatedMessage.getMessage()).isEqualTo(newMessage);
    }

    @Test
    @DisplayName("delete should remove ContactMessage when successful")
    void delete_ShouldRemoveContactMessage_WhenSuccessful() {
        contactMessage.setId(null);
        ContactMessage savedMessage = contactMessageRepository.save(contactMessage);

        Assertions.assertThatCode(() -> contactMessageRepository.delete(savedMessage))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("findById should return Optional of ContactMessage when successful")
    void findById_ShouldReturnOptionalOfContactMessageWhenSuccessful() {
        contactMessage.setId(null);
        ContactMessage savedMessage = contactMessageRepository.save(contactMessage);

        Optional<ContactMessage> contactMessageOptional = contactMessageRepository.findById(savedMessage.getId());

        Assertions.assertThat(contactMessageOptional.isPresent()).isTrue();
        Assertions.assertThat(contactMessageOptional.get()).isEqualTo(savedMessage);
    }

    @Test
    @DisplayName("findById should return empty Optional when there is no ContactMessage")
    void findById_ShouldReturnEmptyOptional_WhenThereIsNoContactMessage() {
        Optional<ContactMessage> contactMessageOptional = contactMessageRepository.findById(1L);

        Assertions.assertThat(contactMessageOptional.isPresent()).isFalse();
    }

    @Test
    @DisplayName("findAllByUser should return Page of ContactMessage when successful")
    void findAllByUser_ShouldReturnPageOfContactMessage_WhenSuccessful() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        User savedUser = userRepository.save(user);
        contactMessage.setUser(savedUser);
        ContactMessage savedMessage = contactMessageRepository.save(contactMessage);

        Page<ContactMessage> messagePage = contactMessageRepository.findAllByUser(user, pageRequest);

        Assertions.assertThat(messagePage).isNotEmpty();
        Assertions.assertThat(messagePage)
                .hasSize(1)
                .contains(savedMessage);
    }

    private ContactMessage createValidContactMessage() {
        return new ContactMessage(
                1L,
                "Tom",
                "cat",
                "tomcat@email.com",
                "12345678910",
                "Hello, this is a nice car, so how can I buy it?",
                false,
                1L,
                null
        );
    }

    private User createValidUser() {
        return User.builder()
                .id(1L)
                .name("Tom")
                .email("tom@email.com")
                .authorities("USER")
                .build();
    }
}