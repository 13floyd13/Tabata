package modele;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.room.Entity;

@Entity
public class Sequence implements {

    //Attributs
    @PrimaryKey(autoGenerate = true)
    private int sequenceId;

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

    //Getters et Setters
    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
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
}
