package tn.esprit.project_spring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.project_spring.entity.Defis;
import tn.esprit.project_spring.repository.DefisRepository;
import tn.esprit.project_spring.services.DefisServices;

import java.util.List;
import java.util.Optional;

@Service
public class DefisServices  {

    @Autowired
    private DefisRepository defisRepository;


    public Defis addDefis(Defis defis) {
        return defisRepository.save(defis);
    }

    public Defis updateIsCompleted(long id, Boolean isCompleted) {
        Optional<Defis> optionalDefis = defisRepository.findById(id);
        if (optionalDefis.isPresent()) {
            Defis defis = optionalDefis.get();
            defis.setIsCompleted(isCompleted);
            return defisRepository.save(defis);
        }
        throw new RuntimeException("Defis not found with id: " + id);
    }

    public List<Defis> getAllDefis() {
        return defisRepository.findAll();
    }

    public void deleteDefis(long id) {
        if (defisRepository.existsById(id)) {
            defisRepository.deleteById(id);
        } else {
            throw new RuntimeException("Defis not found with id: " + id);
        }
    }
}
