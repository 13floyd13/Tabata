package modele;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class Sequence {

    //Attributs
    @PrimaryKey(autoGenerate = true)
    private int sequenceId;

    @ColumnInfo(name = "nom")
    private String nom;

    @ColumnInfo(name = "tempsReposLong")
    private int tempsReposLong;

    @ColumnInfo(name = "description")
    private String description;

    public Sequence(String nom){
        this.nom = nom;
    }

}
