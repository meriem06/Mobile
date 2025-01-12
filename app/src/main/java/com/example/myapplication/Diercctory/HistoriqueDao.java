package com.example.myapplication.Diercctory;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.Entity.HistoriqueTable;

import java.util.List;
@Dao
public interface HistoriqueDao {

    @Insert
    void insert(HistoriqueTable historique);

    @Query("SELECT * FROM historique_table")
    List<HistoriqueTable> getAllHistorique();
    // Nouvelle méthode pour mettre à jour un élément
    @Update
    void update(HistoriqueTable historique);
    // Nouvelle méthode pour supprimer toutes les entrées
    @Query("DELETE FROM historique_table")
    void deleteAll(); // Supprimer toutes les données
    @Delete
    void delete(HistoriqueTable historique);

}
