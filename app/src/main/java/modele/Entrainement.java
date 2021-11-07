package modele;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Entrainement implements Parcelable {

    //Attributs
    @PrimaryKey(autoGenerate = true)
    private int entrainementId;

    @ColumnInfo(name = "nom")
    private String nom;

    @ColumnInfo(name = "tempsPreparation")
    private int tempsPreparation;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "tempsRepos")
    private int tempsRepos;

    //Constructeur
    public Entrainement(String nom){
        this.nom = nom;
    }

    //Getters et Setters

    protected Entrainement(Parcel in) {
        entrainementId = in.readInt();
        nom = in.readString();
        tempsPreparation = in.readInt();
        description = in.readString();
        tempsRepos = in.readInt();
    }

    public static final Creator<Entrainement> CREATOR = new Creator<Entrainement>() {
        @Override
        public Entrainement createFromParcel(Parcel in) {
            return new Entrainement(in);
        }

        @Override
        public Entrainement[] newArray(int size) {
            return new Entrainement[size];
        }
    };

    public int getEntrainementId() {
        return entrainementId;
    }

    public void setEntrainementId(int entrainementId) {
        entrainementId = entrainementId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getTempsPreparation() {
        return tempsPreparation;
    }

    public void setTempsPreparation(int tempsPreparation) {
        this.tempsPreparation = tempsPreparation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTempsRepos() {
        return tempsRepos;
    }

    public void setTempsRepos(int tempsRepos) {
        this.tempsRepos = tempsRepos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(entrainementId);
        dest.writeString(nom);
        dest.writeInt(tempsPreparation);
        dest.writeString(description);
        dest.writeInt(tempsRepos);
    }
}
