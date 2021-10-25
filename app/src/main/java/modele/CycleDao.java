package modele;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CycleDao {

    @Transaction
    @Query("SELECT * FROM Cycle")
    List<Travail> getAll();

    @Insert
    void insert(Cycle cycle);

    @Delete
    void delete(Cycle cycle);

    @Update
    void update(Cycle cycle);
}
