package data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import modele.CycleTravailCrossRef;
import modele.SequenceAvecCycles;
import modele.SequenceCycleCrossRef;

@Dao
public interface SequenceCycleCrossRefDao {

    @Transaction
    @Query("Select * FROM Sequence")
    List<SequenceAvecCycles> getSequenceCycle();

    @Transaction
    @Query("SELECT cycleId FROM SEQUENCECYCLECROSSREF WHERE sequenceId = :sequenceId ")
    List<Long> getCyclesId(long sequenceId);

    @Insert
    long insert(SequenceCycleCrossRef sequenceCycleCrossRef);

    @Delete
    void delete(SequenceCycleCrossRef sequenceCycleCrossRef);

    @Update
    void update(SequenceCycleCrossRef sequenceCycleCrossRef);
}
