package tn.esprit.project_spring.repository;

import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.project_spring.entity.UserTest;

import java.util.List;

@Repository

public interface IUserRepo  extends JpaRepository<UserTest , Long> {
    public List<UserTest> findAll();

}
