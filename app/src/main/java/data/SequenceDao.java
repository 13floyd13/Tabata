package data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;


import modele.Sequence;
import modele.SequenceAvecCycles;

@Dao
public interface SequenceDao {

        @Transaction
        @Query("SELECT * FROM Sequence")
        List<SequenceAvecCycles> getAll();

        @Insert
        void insert(Sequence sequence);

        @Delete
        void delete(Sequence sequence);

        @Update
        void update(Sequence sequence);

}
