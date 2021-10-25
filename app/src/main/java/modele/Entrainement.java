package modele;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Entrainement {

    //Attributs
    @PrimaryKey(autoGenerate = true)
    private int entrainementId;

    @ColumnInfo(name = "nom")
    private String nom;

    @ColumnInfo(name = "tempsPreparation")
    private int tempsPreparation;

    @ColumnInfo(name = "description")
    private String description;

    //Constructeur
    public Entrainement(String nom){
        this.nom = nom;
    }

    //Getters et Setters

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
}
