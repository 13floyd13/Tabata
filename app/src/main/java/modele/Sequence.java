package modele;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.room.Entity;

@Entity
public class Sequence implements Parcelable {

    //Attributs
    @PrimaryKey(autoGenerate = true)
    private long sequenceId;

    @ColumnInfo(name = "nom")
    private String nom;

    @ColumnInfo(name = "tempsReposLong")
    private int tempsReposLong;

    @ColumnInfo(name = "description")
    private String description;

    //Constructeur
    public Sequence(String nom){
        this.nom = nom;
    }

    protected Sequence(Parcel in) {
        sequenceId = in.readLong();
        nom = in.readString();
        tempsReposLong = in.readInt();
        description = in.readString();
    }

    public static final Creator<Sequence> CREATOR = new Creator<Sequence>() {
        @Override
        public Sequence createFromParcel(Parcel in) {
            return new Sequence(in);
        }

        @Override
        public Sequence[] newArray(int size) {
            return new Sequence[size];
        }
    };

    //Getters et Setters
    public long getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(long sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getTempsReposLong() {
        return tempsReposLong;
    }

    public void setTempsReposLong(int tempsReposLong) {
        this.tempsReposLong = tempsReposLong;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(sequenceId);
        dest.writeString(nom);
        dest.writeInt(tempsReposLong);
        dest.writeString(description);
    }
}
