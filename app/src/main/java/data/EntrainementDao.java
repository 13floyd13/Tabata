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
import modele.Sequence;


@Dao
public interface EntrainementDao {

    @Transaction
    @Query("SELECT * FROM  Entrainement")
    List<EntrainementAvecSequences> getAll();

    @Transaction
    @Insert
    void insert(Entrainement entrainement);

    @Insert
    void insertSequences(List<Sequence> sequences);

    @Delete
    void delete(Entrainement entrainement);

    @Update
    void update(Entrainement entrainement);
}
