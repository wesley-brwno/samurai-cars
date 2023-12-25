package br.com.project.samuraicars.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id", callSuper = true)
public class ContactMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String message;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
