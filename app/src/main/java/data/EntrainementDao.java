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
    @Query("SELECT * FROM Entrainement WHERE entrainementId IN (:entrainementId)")
    List<Entrainement> getEntrainements(List<Long> entrainementId);

    @Transaction
    @Insert
    long insert(Entrainement entrainement);

    @Insert
    void insertSequences(List<Sequence> sequences);

    @Delete
    void delete(Entrainement entrainement);

    @Delete
    void deleteAll(List<Entrainement> entrainements);

    @Update
    void update(Entrainement entrainement);
}
