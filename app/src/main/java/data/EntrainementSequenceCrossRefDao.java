package data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import modele.EntrainementAvecSequences;
import modele.EntrainementSequenceCrossRef;
import modele.SequenceCycleCrossRef;

@Dao
public interface EntrainementSequenceCrossRefDao {

    @Transaction
    @Query("Select * From Entrainement")
    List<EntrainementAvecSequences> getEntrainementSequence();

    @Insert
    long insert(EntrainementSequenceCrossRef entrainementSequenceCrossRef);

    @Delete
    void delete(SequenceCycleCrossRef sequenceCycleCrossRef);

    @Update
    void update(SequenceCycleCrossRef sequenceCycleCrossRef);
}
