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
import modele.Sequence;
import modele.SequenceAvecCycles;

@Dao
public interface SequenceDao {

        @Transaction
        @Query("SELECT * FROM Sequence")
        List<SequenceAvecCycles> getAll();

        @Transaction
        @Query("SELECT repetition FROM sequence WHERE sequenceId =:sequenceId")
        int getRepetitions(long sequenceId);

        @Transaction
        @Query("SELECT * FROM sequence WHERE sequenceId IN (:sequenceId)")
        List<Sequence> getSequences(List<Long> sequenceId);

        @Transaction
        @Insert
        long insert(Sequence sequence);

        @Insert
        void insertCycles(List<Cycle> cycles);

        @Delete
        void delete(Sequence sequence);

        @Delete
        void deleteAll(ArrayList<Sequence> sequences);

        @Update
        void update(Sequence sequence);

}
