package br.com.project.samuraicars.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
public class Vehicle extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String modelo;
    private Long year;
    @ManyToOne
    @JoinColumn(name = "sys-user-id")
    private User user;

    @OneToMany(mappedBy = "vehicle")
    private List<VehichlePhoto> photos;
}
