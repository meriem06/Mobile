package tn.esprit.project_spring.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.project_spring.entity.Luminosite;
import tn.esprit.project_spring.repository.ILuminositeRepo;


import java.util.List;


@AllArgsConstructor
@Service
public class LuminositeImp implements ILuminositeService {
    private ILuminositeRepo luminositerepo;

    @Override
    public long insert(Luminosite luminosite) {
        Luminosite luminosite1 =luminositerepo.save(luminosite);
        return luminosite1.getId() ;
    }

    @Override
    public List<Luminosite> getAll() {
        return luminositerepo.findAll();
    }

    @Override
    public void deleteLuminosite(int id) {
        luminositerepo.delete(luminositerepo.findById(id).get());

    }

    @Override
    public void updateLuminosite(Luminosite luminosite) {
        luminositerepo.save(luminosite);

    }

    @Override
    public Luminosite getLuminositeById(int id) {
        return luminositerepo.findById(id).get();
    }
}
