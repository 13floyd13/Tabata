package modele;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Cycle implements Parcelable {

    //Attributs
    @PrimaryKey(autoGenerate = true)
    private int cycleId;

    @ColumnInfo(name = "nom")
    private String nom;

    public Cycle(String nom){
        this.nom = nom;
    }

    protected Cycle(Parcel in) {
        cycleId = in.readInt();
        nom = in.readString();
    }

    public static final Creator<Cycle> CREATOR = new Creator<Cycle>() {
        @Override
        public Cycle createFromParcel(Parcel in) {
            return new Cycle(in);
        }

        @Override
        public Cycle[] newArray(int size) {
            return new Cycle[size];
        }
    };

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getCycleId() {
        return cycleId;
    }

    public void setCycleId(int id) {
        this.cycleId = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cycleId);
        dest.writeString(nom);
    }
}
