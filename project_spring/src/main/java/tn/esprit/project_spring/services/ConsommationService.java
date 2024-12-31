package tn.esprit.project_spring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.project_spring.entity.Consommation;
import tn.esprit.project_spring.repository.ConsommationRepository;

import java.util.List;

@Service
public class ConsommationService {

    @Autowired
    private ConsommationRepository consommationRepository;

    public Consommation addConsommation(Consommation consommation) {
        return consommationRepository.save(consommation);
    }

    public void deleteAllConsommations() {
        consommationRepository.deleteAll();
    }

    public List<Consommation> getConsommationsSortedByCost() {
        return consommationRepository.findAllOrderByCost();
    }

    public List<Consommation> getConsommationsSortedByDistance() {
        return consommationRepository.findAllOrderByDistance();
    }
}
