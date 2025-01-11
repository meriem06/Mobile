package tn.esprit.project_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.project_spring.entity.Luminosite;


@Repository
public interface ILuminositeRepo extends JpaRepository<Luminosite, Integer> {
}
