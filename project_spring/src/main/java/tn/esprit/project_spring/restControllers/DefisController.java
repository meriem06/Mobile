package tn.esprit.project_spring.restControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.project_spring.entity.Defis;
import tn.esprit.project_spring.services.DefisServices;

import java.util.List;

@RestController
@RequestMapping("/defis")
public class DefisController {

    @Autowired
    private DefisServices defisService;


    @PostMapping("/add")
    public ResponseEntity<String> addDefis(@RequestBody Defis defis) {
        defisService.addDefis(defis);
        return ResponseEntity.ok("Defis added successfully!");
    }


    @PutMapping("/update-is-completed/{id}")
    public ResponseEntity<String> updateIsCompleted(@PathVariable long id, @RequestBody Boolean isCompleted) {
        defisService.updateIsCompleted(id, isCompleted);
        return ResponseEntity.ok("Defis updated successfully!");
    }


    @GetMapping("/all")
    public List<Defis> getAllDefis() {
        return defisService.getAllDefis();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDefis(@PathVariable long id) {
        defisService.deleteDefis(id);
        return ResponseEntity.ok("Defis deleted successfully!");
    }
}
