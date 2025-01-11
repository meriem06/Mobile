package tn.esprit.project_spring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String birthdate;
    private String password;
    private String email;
    private boolean isVerified;
    private String profileImagePath;
    @OneToMany
    List<Luminosite> luminosites ;
}
