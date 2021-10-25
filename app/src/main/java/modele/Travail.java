package modele;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Travail implements Serializable {

    //Attributs
    @PrimaryKey(autoGenerate = true)
    private int travailId;

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
}
