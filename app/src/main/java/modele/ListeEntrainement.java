package modele;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ListeEntrainement {

    //Attributs
    @PrimaryKey(autoGenerate = true)
    private int listeEntrainementId;

    public ListeEntrainement(){}

    //Getter et Setter

    public int getListeEntrainementId() {
        return listeEntrainementId;
    }

    public void setListeEntrainementId(int listeEntrainementId) {
        listeEntrainementId = listeEntrainementId;
    }
}
