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

    @Query("SELECT * FROM Travail WHERE travailId IN (:ids)")
    List<Travail> getTravails(List<Long> ids);

    @Insert
    long insert(Travail travail);

    @Delete
    void delete(Travail travail);

    @Update
    void update(Travail travail);
}
