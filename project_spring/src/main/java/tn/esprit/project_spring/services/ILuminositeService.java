package tn.esprit.project_spring.services;



import tn.esprit.project_spring.entity.Luminosite;

import java.util.List;

public interface ILuminositeService {

    long insert(Luminosite luminosite);

    List<Luminosite> getAll();

    void deleteLuminosite(int id);


    void updateLuminosite(Luminosite luminosite);

    Luminosite getLuminositeById(int id);
}
