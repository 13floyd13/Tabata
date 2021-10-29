package data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;


import modele.Cycle;
import modele.Sequence;
import modele.SequenceAvecCycles;

@Dao
public interface SequenceDao {

        @Transaction
        @Query("SELECT * FROM Sequence")
        List<Sequence> getAll();

        @Transaction
        @Insert
        void insert(Sequence sequence);

        @Insert
        void insertCycles(List<Cycle> cycles);

        @Delete
        void delete(Sequence sequence);

        @Update
        void update(Sequence sequence);

}
