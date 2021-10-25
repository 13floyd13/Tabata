package modele;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Cycle {

    //Attributs
    @PrimaryKey(autoGenerate = true)
    private int cycleId;

    @ColumnInfo(name = "nom")
    private String nom;

    public Cycle(String nom){
        this.nom = nom;
    }

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
}
