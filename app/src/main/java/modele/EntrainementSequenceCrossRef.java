package modele;

import androidx.room.Entity;

@Entity(primaryKeys = {"entrainementId", "sequenceId"})
public class EntrainementSequenceCrossRef {
    public long entrainementId;
    public long sequenceId;
}
