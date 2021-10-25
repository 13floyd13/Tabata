package modele;

import androidx.room.Database;
import androidx.room.RoomDatabase;



@Database(entities = {Travail.class, Cycle.class}, version = 1, exportSchema = false)
public abstract class AppDatabase  extends RoomDatabase{

    public abstract TravailDao travailDao();
    public abstract CycleDao cycleDao();
}
