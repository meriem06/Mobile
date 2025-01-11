package tn.esprit.project_spring.entity;

import jakarta.persistence.*;
import lombok.*;

;

@Getter
@Setter
@Table(name = "Luminosite ")
@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
public class Luminosite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    private float intensite;
    private boolean isNormal;

    private String date;
    @Transient
    int userId;
    @ManyToOne
    User user ;
}
