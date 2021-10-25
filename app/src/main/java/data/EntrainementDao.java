package data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;


import modele.Entrainement;
import modele.EntrainementAvecSequences;


@Dao
public interface EntrainementDao {

    @Transaction
    @Query("SELECT * FROM Entrainement")
    List<EntrainementAvecSequences> getAll();

    @Insert
    void insert(Entrainement entrainement);

    @Delete
    void delete(Entrainement entrainement);

    @Update
    void update(Entrainement entrainement);
}
