package tn.esprit.project_spring.restControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.project_spring.entity.Luminosite;
import tn.esprit.project_spring.services.ILuminositeService;


import java.util.List;

@RestController
@RequestMapping("/luminosite")
public class LuminositeControllerRest {
    @Autowired
    ILuminositeService luminositeService;
    @PostMapping(path = "/add")
   public long insert(@RequestBody Luminosite luminosite)
    {
        return luminositeService.insert(luminosite);
    }
    @GetMapping(path = "/get")
   public List<Luminosite> getAll()
    {
        return luminositeService.getAll();
    }
@DeleteMapping(path = "/delete/{id}")
 public void deleteLuminosite(@PathVariable("id")int id)
{
 luminositeService.deleteLuminosite(id);
}

@PutMapping(path = "/update")
 public void updateLuminosite(@RequestBody Luminosite luminosite)
{
    luminositeService.updateLuminosite(luminosite);
}
@GetMapping(path = "/{id}")
    public Luminosite getLuminositeById(@PathVariable("id")int id)
    {
        return luminositeService.getLuminositeById(id);
    }
}
