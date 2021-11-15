package modele;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Travail implements Parcelable {

    //Attributs
    @PrimaryKey(autoGenerate = true)
    private int travailId;

    @ColumnInfo(name = "cycleId")
    private long cycleId;

    @ColumnInfo(name = "nom")
    private String nom;

    @ColumnInfo(name = "temps")
    private int temps;

    @ColumnInfo(name = "repos")
    private int repos;

    //Constructeur
    public Travail(String nom, int temps, int repos){
        this.nom = nom;
        this.temps = temps;
        this.repos = repos;
    }

    protected Travail(Parcel in) {
        travailId = in.readInt();
        nom = in.readString();
        temps = in.readInt();
        repos = in.readInt();
    }

    public static final Creator<Travail> CREATOR = new Creator<Travail>() {
        @Override
        public Travail createFromParcel(Parcel in) {
            return new Travail(in);
        }

        @Override
        public Travail[] newArray(int size) {
            return new Travail[size];
        }
    };

    //Getters et Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getTemps() {
        return temps;
    }

    public void setTemps(int temps) {
        this.temps = temps;
    }

    public int getTravailId() {
        return travailId;
    }

    public void setTravailId(int id) {
        this.travailId = id;
    }

    public int getRepos() {
        return repos;
    }

    public void setRepos(int repos) {
        this.repos = repos;
    }

    public long getCycleId() {
        return cycleId;
    }

    public void setCycleId(long cycleId) {
        this.cycleId = cycleId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(travailId);
        dest.writeString(nom);
        dest.writeInt(temps);
        dest.writeInt(repos);
    }
}
