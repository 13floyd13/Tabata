package modele;

import androidx.room.Entity;

@Entity(primaryKeys = {"sequenceId", "cycleId"})
public class SequenceCycleCrossRef {
    public long sequenceId;
    public long cycleId;
}
