package br.com.project.samuraicars.model;

import jakarta.persistence.*;

import java.sql.Blob;

@Entity
@Table(name = "vehicle-photos")
public class VehichlePhoto extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Lob
    private Blob image;

    @ManyToOne
    @JoinColumn(name = "vehicle-id")
    private Vehicle vehicle;
}
