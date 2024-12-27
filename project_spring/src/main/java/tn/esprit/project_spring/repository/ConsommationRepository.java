package tn.esprit.project_spring.repository;

 import org.springframework.data.jpa.repository.JpaRepository;
 import tn.esprit.project_spring.entity.Consommation;

public interface ConsommationRepository extends JpaRepository<Consommation, Integer> {
}
