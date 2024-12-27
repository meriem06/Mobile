package tn.esprit.project_spring.restControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.project_spring.entity.User;
import tn.esprit.project_spring.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        User user = userService.login(email, password);
        if (user != null && user.isVerified()) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(401).body("Invalid credentials or account not verified");
    }

    //ajout *******************************************
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }
    @PutMapping("/update-password/{id}")
    public ResponseEntity<String> updatePassword(@PathVariable int id, @RequestParam String password) {
        userService.updateUserPassword(id, password);
        return ResponseEntity.ok("Password updated successfully");
    }

    @PutMapping("/update-image/{id}")
    public ResponseEntity<String> updateImagePath(@PathVariable int id, @RequestParam String imagePath) {
        userService.updateUserImagePath(id, imagePath);
        return ResponseEntity.ok("Profile image path updated successfully");
    }

    @PutMapping("/update-username/{id}")
    public ResponseEntity<String> updateUsername(@PathVariable int id, @RequestParam String name) {
        userService.updateUserName(id, name);
        return ResponseEntity.ok("Username updated successfully");
    }

    @PutMapping("/update-email/{id}")
    public ResponseEntity<String> updateEmail(@PathVariable int id, @RequestParam String newEmail) {
        userService.updateUserEmail(id, newEmail);
        return ResponseEntity.ok("Email updated successfully");
    }

    @PutMapping("/update-birthdate/{id}")
    public ResponseEntity<String> updateBirthdate(@PathVariable int id, @RequestParam String birthdate) {
        userService.updateUserBirthdate(id, birthdate);
        return ResponseEntity.ok("Birthdate updated successfully");
    }
}
