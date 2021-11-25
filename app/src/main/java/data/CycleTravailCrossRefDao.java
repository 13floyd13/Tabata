package data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import modele.CycleAvecTravails;
import modele.CycleTravailCrossRef;

@Dao
public interface CycleTravailCrossRefDao {

    @Transaction
    @Query("Select * FROM Cycle")
    List<CycleAvecTravails> getCycleTravail();

    @Transaction
    @Query("Select travailId FROM CycleTravailCrossRef WHERE cycleId IN (:cycleId)")
    List<Long> getTravailIds(long cycleId);

    @Transaction
    @Query("Select cycleId FROM CycleTravailCrossRef WHERE travailId IN (:travailId)")
    List<Long> getCycleIds(long travailId);
    @Insert
    long insert(CycleTravailCrossRef cycleTravailCrossRef);

    @Delete
    void delete(CycleTravailCrossRef cycleTravailCrossRef);

    @Update
    void update(CycleTravailCrossRef cycleTravailCrossRef);
}
