package br.com.project.samuraicars.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Blob;

@Entity
@Table(name = "vehicle-photos")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class VehiclePhoto extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Lob
    private Blob image;

    @ManyToOne
    @JoinColumn(name = "vehicle-id")
    private Vehicle vehicle;

    public VehiclePhoto(String name, Blob image, Vehicle vehicle) {
        this.name = name;
        this.image = image;
        this.vehicle = vehicle;
    }
}
