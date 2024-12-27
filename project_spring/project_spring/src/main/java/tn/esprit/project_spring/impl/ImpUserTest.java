package tn.esprit.project_spring.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.project_spring.entity.UserTest;
import tn.esprit.project_spring.repository.IUserRepo;
import tn.esprit.project_spring.services.IUserService;

import java.util.List;
@Slf4j
@AllArgsConstructor
@Service
public class ImpUserTest implements IUserService {
    IUserRepo userRepo;

    @Override
    public List<UserTest> getallusers() {
        log.info("mcheeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        return  userRepo.findAll();
    }

    @Override
    public UserTest adduser(UserTest userTest) {
        return userRepo.save(userTest);
    }
}
