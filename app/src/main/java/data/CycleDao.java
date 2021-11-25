package data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

import modele.Cycle;
import modele.CycleAvecTravails;
import modele.CycleTravailCrossRef;
import modele.Travail;

@Dao
public interface CycleDao {

    @Transaction
    @Query("SELECT * FROM Cycle")
    List<CycleAvecTravails> getAll();

    @Transaction
    @Query("SELECT * FROM Cycle WHERE cycleId In (:cycleIds)")
    List<Cycle> getCycles(List<Long> cycleIds);

    @Transaction
    @Query("SELECT * FROM Cycle WHERE cycleId In (:cycleIds)")
    ArrayList<CycleAvecTravails> getCyclesAvecTravails(List<Long> cycleIds);

    @Transaction
    @Insert
    long insert(Cycle cycle);

    @Insert
    void insertTravails(List<Travail> travails);

    @Insert
    void insertCycleTravail(CycleTravailCrossRef cycleTravailCrossRef);

    @Delete
    void delete(Cycle cycle);

    @Update
    void update(Cycle cycle);
}
