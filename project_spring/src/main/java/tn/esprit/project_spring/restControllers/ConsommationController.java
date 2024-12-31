package tn.esprit.project_spring.restControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.project_spring.entity.Consommation;
import tn.esprit.project_spring.services.ConsommationService;

import java.util.List;

@RestController
@RequestMapping("/consommations")
public class ConsommationController {

    @Autowired
    private ConsommationService consommationService;

    @PostMapping("/add")
    public Consommation addConsommation(@RequestBody Consommation consommation) {
        return consommationService.addConsommation(consommation);
    }

    @DeleteMapping("/delete-all")
    public void deleteAllConsommations() {
        consommationService.deleteAllConsommations();
    }

    @GetMapping("/sorted-by-cost")
    public List<Consommation> getConsommationsSortedByCost() {
        return consommationService.getConsommationsSortedByCost();
    }

    @GetMapping("/sorted-by-distance")
    public List<Consommation> getConsommationsSortedByDistance() {
        return consommationService.getConsommationsSortedByDistance();
    }
}
