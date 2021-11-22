package data;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import modele.Historique;

@Dao
public interface HistoriqueDao {

    @Query("SELECT * FROM Historique")
    List<Historique> getAll();

    @Insert
    long insert(Historique historique);

    @Delete
    void delete(Historique historique);

    @Update
    void update(Historique historique);
}
