package tn.esprit.project_spring.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.project_spring.entity.User;
@Repository

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmailAndPassword(String email, String password);
    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id = :id")
    User findById(int id);

    @Query("UPDATE User u SET u.name = :name WHERE u.id = :id")
    void updateUserName(int id, String name);

    @Query("UPDATE User u SET u.email = :newEmail WHERE u.id = :id")
    void updateUserEmail(String newEmail, int id);

    @Query("UPDATE User u SET u.birthdate = :birthdate WHERE u.id = :id")
    void updateUserBirthdate(int id, String birthdate);

    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    void updateUserPassword(int id, String password);

    @Query("UPDATE User u SET u.profileImagePath = :imagePath WHERE u.id = :id")
    void updateUserImagePath(int id, String imagePath);


    @Query("UPDATE User u SET u.isVerified = :isVerified WHERE u.email = :email")
    void updateUserVerificationStatus(String email, boolean isVerified);
}
