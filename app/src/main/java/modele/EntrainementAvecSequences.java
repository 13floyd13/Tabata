package modele;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;


public class EntrainementAvecSequences implements Parcelable {
    @Embedded
    public Entrainement entrainement;

    //Relation de type many-to-many entre Entrainement et Sequence
    @Relation(
            parentColumn = "entrainementId",
            entityColumn = "sequenceId",
            associateBy = @Junction(EntrainementSequenceCrossRef.class)
    )
    private List<Sequence> sequences ;

    //Constructeur
    public EntrainementAvecSequences(Entrainement entrainement, List<Sequence> sequences){
        this.entrainement = entrainement;
        this.sequences = sequences;
    }

    //Méthodes

    protected EntrainementAvecSequences(Parcel in) {
        sequences = in.createTypedArrayList(Sequence.CREATOR);
    }

    public static final Creator<EntrainementAvecSequences> CREATOR = new Creator<EntrainementAvecSequences>() {
        @Override
        public EntrainementAvecSequences createFromParcel(Parcel in) {
            return new EntrainementAvecSequences(in);
        }

        @Override
        public EntrainementAvecSequences[] newArray(int size) {
            return new EntrainementAvecSequences[size];
        }
    };

    //Ajout d'une séquence à un entrainement
    public void addSequence(Sequence sequence){
        sequences.add(sequence);
    }

    //suppression d'une séquence de l'entrainement
    public void removeSequence(Sequence sequence){
        sequences.remove(sequence);
    }

    //Getters

    public Entrainement getEntrainement() {
        return entrainement;
    }

    public List<Sequence> getSequences() {
        return sequences;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(sequences);
    }
}
