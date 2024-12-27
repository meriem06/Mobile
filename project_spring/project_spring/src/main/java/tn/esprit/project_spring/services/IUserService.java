package tn.esprit.project_spring.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.project_spring.entity.UserTest;

import java.util.List;


public interface IUserService {
    public List<UserTest> getallusers();
    UserTest adduser(UserTest userTest);
}
