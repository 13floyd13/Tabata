package data;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

import modele.Travail;

@Dao
public interface TravailDao {


    @Query("SELECT * FROM Travail")
    List<Travail> getAll();

    @Insert
    void insert(Travail travail);

    @Delete
    void delete(Travail travail);

    @Update
    void update(Travail travail);
}
