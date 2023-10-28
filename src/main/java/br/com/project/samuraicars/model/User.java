package br.com.project.samuraicars.model;

import br.com.project.samuraicars.DTO.user.UserPostRequestBody;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "system_users")
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private String password;
    @OneToMany(mappedBy = "user")
    private List<Vehicle> vehicles;

    public User(UserPostRequestBody data) {
        this.name = data.name();
        this.email = data.email();
        this.password = data.password();
    }

//    TODO: implement interface UserDetails to configure Authorization and Authentication
}
