package br.com.project.samuraicars.model;

import br.com.project.samuraicars.DTO.vehicle.VehiclePostRequestBody;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
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
    private List<VehiclePhoto> photos;

    public Vehicle(VehiclePostRequestBody vehiclePostRequestBody, UserDetails user) {
        this.name = vehiclePostRequestBody.name();
        this.modelo = vehiclePostRequestBody.model();
        this.year = vehiclePostRequestBody.year();
        this.user = (User) user;
    }
}
