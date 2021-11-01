package data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import modele.Cycle;
import modele.CycleAvecTravails;
import modele.Travail;

@Dao
public interface CycleDao {

    @Transaction
    @Query("SELECT * FROM Cycle")
    List<Cycle> getAll();

    @Transaction
    @Insert
    void insert(Cycle cycle);

    @Insert
    void insertTravails(List<Travail> travails);

    @Delete
    void delete(Cycle cycle);

    @Update
    void update(Cycle cycle);
}
