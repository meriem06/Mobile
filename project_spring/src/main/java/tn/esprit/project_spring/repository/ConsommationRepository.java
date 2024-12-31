package tn.esprit.project_spring.repository;

 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.data.jpa.repository.Query;
 import tn.esprit.project_spring.entity.Consommation;

 import java.util.List;

public interface ConsommationRepository extends JpaRepository<Consommation, Integer> {
 @Query("SELECT c FROM Consommation c ORDER BY c.cost ASC")
 List<Consommation> findAllOrderByCost();

 // Custom query to sort by distance
 @Query("SELECT c FROM Consommation c ORDER BY c.distance ASC")
 List<Consommation> findAllOrderByDistance();
}
