package modele;

import androidx.room.Entity;

import java.util.ArrayList;

@Entity(primaryKeys = {"travailId", "cycleId"})
public class CycleTravailCrossRef {
    public long cycleId;
    public long travailId;
}
