package modele;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class SequenceAvecCycles implements Parcelable {
    @Embedded
    public Sequence sequence;

    //@Embedded
    //private Integer nbRepet;

    //Relation de type many-to-many entre Sequence et Cycle
    @Relation(
            parentColumn = "sequenceId",
            entityColumn = "cycleId",
            associateBy = @Junction(SequenceCycleCrossRef.class)
    )
    public List<Cycle> cycles;

    //Constructeur
    public SequenceAvecCycles(Sequence sequence, List<Cycle> cycles){
        this.sequence = sequence;
        this.cycles = cycles;
        //this.nbRepet = sequence.getRepetition();
    }

    //Méthodes

    protected SequenceAvecCycles(Parcel in) {
        sequence = in.readParcelable(Sequence.class.getClassLoader());
        cycles = in.createTypedArrayList(Cycle.CREATOR);
    }

   /* @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(sequence, flags);
        dest.writeTypedList(cycles);

    }*/

    /*@Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SequenceAvecCycles> CREATOR = new Creator<SequenceAvecCycles>() {
        @Override
        public SequenceAvecCycles createFromParcel(Parcel in) {
            return new SequenceAvecCycles(in);
        }

        @Override
        public SequenceAvecCycles[] newArray(int size) {
            return new SequenceAvecCycles[size];
        }
    };*/

    public static final Creator<SequenceAvecCycles> CREATOR = new Creator<SequenceAvecCycles>() {
        @Override
        public SequenceAvecCycles createFromParcel(Parcel in) {
            return new SequenceAvecCycles(in);
        }

        @Override
        public SequenceAvecCycles[] newArray(int size) {
            return new SequenceAvecCycles[size];
        }
    };

    //Ajout d'un cycle à la séquence
    public void addCycle(Cycle cycle){
        cycles.add(cycle);
    }

    //suppression d'un cycle de la séquence
    public void removeCycle(Cycle cycle){
        cycles.remove(cycle);
    }

    //Getters
    public Sequence getSequence() {
        return sequence;
    }

    public List<Cycle> getCycles() {
        return cycles;
    }

    public int getNbRepet() {
        return sequence.getRepetition();
    }

    public void setNbRepet(int nbRepet) {
        sequence.setRepetition(nbRepet);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(sequence, flags);
        //dest.writeInt(nbRepet);
        dest.writeTypedList(cycles);
    }
}
