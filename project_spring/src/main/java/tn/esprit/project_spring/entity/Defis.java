package tn.esprit.project_spring.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "defis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Defis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idDefi;

    private String descriptionDefi;
    private Boolean isCompleted;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
