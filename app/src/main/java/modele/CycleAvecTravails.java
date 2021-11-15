package modele;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CycleAvecTravails implements Parcelable {
    @Embedded
    public Cycle cycle;

    //Relation de type one-to-many entre cycle et travail
    @Relation(
            parentColumn = "cycleId",
            entityColumn = "cycleId"
    )
    public List<Travail> travails;

    //Constructeur
    public CycleAvecTravails(Cycle cycle, List<Travail> travails){
        this.cycle = cycle;
        this.travails = travails;
    }

    //Méthodes

    protected CycleAvecTravails(Parcel in) {
        cycle = in.readParcelable(Cycle.class.getClassLoader());
        travails = in.createTypedArrayList(Travail.CREATOR);
    }

    public static final Creator<CycleAvecTravails> CREATOR = new Creator<CycleAvecTravails>() {
        @Override
        public CycleAvecTravails createFromParcel(Parcel in) {
            return new CycleAvecTravails(in);
        }

        @Override
        public CycleAvecTravails[] newArray(int size) {
            return new CycleAvecTravails[size];
        }
    };

    //Ajout d'un travail au cycle
    public void addTravail(Travail travail){
        travails.add(travail);
    }

    //Suppression d'un travail du cycle
    public void removeTravail(Travail travail){
        travails.remove(travail);
    }

    //Getters
    public List<Travail> getTravails() {
        return travails;
    }

    public Cycle getCycle() {
        return cycle;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(cycle, flags);
        dest.writeTypedList(travails);
    }
}
