package tn.esprit.project_spring.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "consommations")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Consommation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String distance;
    private String place;
    private String cost;
    private String time;
}
