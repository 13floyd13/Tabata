package data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import modele.Cycle;
import modele.Entrainement;
import modele.Sequence;
import modele.Travail;


@Database(entities = {Travail.class, Cycle.class, Entrainement.class, Sequence.class}, version = 1, exportSchema = false)
public abstract class AppDatabase  extends RoomDatabase{

    public abstract TravailDao travailDao();
    public abstract CycleDao cycleDao();
    public abstract EntrainementDao entrainementDao();
    public abstract SequenceDao sequenceDao();
}
