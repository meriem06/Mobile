package tn.esprit.project_spring.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.project_spring.entity.User;
import tn.esprit.project_spring.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    public User registerUser(User user) {
        return userRepository.save(user);
    }
    public User login(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    // Update user's name
    public void updateUserName(int id, String name) {
        userRepository.updateUserName(id, name);
    }

    // Update user's email
    public void updateUserEmail(int id, String newEmail) {
        userRepository.updateUserEmail(newEmail, id);
    }

    // Update user's birthdate
    public void updateUserBirthdate(int id, String birthdate) {
        userRepository.updateUserBirthdate(id, birthdate);
    }

    // Update user's password
    public void updateUserPassword(int id, String password) {
        userRepository.updateUserPassword(id, password);
    }

    // Update user's profile image path
    public void updateUserImagePath(int id, String imagePath) {
        userRepository.updateUserImagePath(id, imagePath);
    }
}
