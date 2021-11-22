package modele;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Historique {

    @PrimaryKey(autoGenerate = true)
    private long historiqueId;

    @ColumnInfo(name = "nomEntrainement")
    private String nomEntrainement;

    @ColumnInfo(name = "date")
    private String date;

    public Historique(String nomEntrainement, String date){

        this.nomEntrainement = nomEntrainement;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public long getHistoriqueId() {
        return historiqueId;
    }

    public String getNomEntrainement() {
        return nomEntrainement;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHistoriqueId(long historiqueId) {
        this.historiqueId = historiqueId;
    }

    public void setNomEntrainement(String nomEntrainement) {
        this.nomEntrainement = nomEntrainement;
    }
}
