package data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import modele.Cycle;
import modele.CycleTravailCrossRef;
import modele.Entrainement;
import modele.EntrainementSequenceCrossRef;
import modele.Historique;
import modele.Sequence;
import modele.SequenceCycleCrossRef;
import modele.Travail;


@Database(entities = {Travail.class, Cycle.class, Entrainement.class, Sequence.class, CycleTravailCrossRef.class, SequenceCycleCrossRef.class, EntrainementSequenceCrossRef.class, Historique.class}, version = 1, exportSchema = false)
public abstract class AppDatabase  extends RoomDatabase{

    public abstract TravailDao travailDao();
    public abstract CycleDao cycleDao();
    public abstract EntrainementDao entrainementDao();
    public abstract SequenceDao sequenceDao();
    public abstract CycleTravailCrossRefDao cycleTravailCrossRefDao();
    public abstract SequenceCycleCrossRefDao sequenceCycleCrossRefDao();
    public abstract EntrainementSequenceCrossRefDao entrainementSequenceCrossRefDao();
    public abstract HistoriqueDao historiqueDao();
}
