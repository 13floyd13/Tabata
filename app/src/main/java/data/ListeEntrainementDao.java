package data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;


import modele.ListeEntrainement;
import modele.ListeEntrainementAvecEntrainements;

@Dao
public interface ListeEntrainementDao {

    @Transaction
    @Query("SELECT * FROM ListeEntrainement")
    List<ListeEntrainementAvecEntrainements> getAll();

    @Insert
    void insert(ListeEntrainement listeEntrainement);

    @Delete
    void delete(ListeEntrainement listeEntrainement);

    @Update
    void update(ListeEntrainement listeEntrainement);
}
