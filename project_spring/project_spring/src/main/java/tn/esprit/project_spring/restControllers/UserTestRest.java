package tn.esprit.project_spring.restControllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.project_spring.entity.UserTest;
import tn.esprit.project_spring.impl.ImpUserTest;
import tn.esprit.project_spring.services.IUserService;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserTestRest {
    ImpUserTest service ;


    @GetMapping("/users")
    public List<UserTest> hetUsers(){
      return  service.getallusers();
    }
    @PostMapping("/add")
    public UserTest ziduser(@RequestBody UserTest user){
        return service.adduser(user);
    }
}
