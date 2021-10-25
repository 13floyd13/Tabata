package modele;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ListeEntrainementAvecEntrainements {

    @Embedded
    public ListeEntrainement listeEntrainement;

    //Relation de type one-to-many entre ListeEntrainement et Entrainement
    @Relation(
            parentColumn = "listeEntrainementId",
            entityColumn = "entrainementId"
    )
    public List<Entrainement> entrainements;

    //Constructeur
    public ListeEntrainementAvecEntrainements(ListeEntrainement listeEntrainement, List<Entrainement> entrainements){
        this.listeEntrainement = listeEntrainement;
        this.entrainements = entrainements;
    }

    //Méthodes

    //Ajout d'un Entrainement à ListeEntrainement
    public void addEntrainement(Entrainement entrainement){
        entrainements.add(entrainement);
    }

    //suppression d'un entrainement de la ListeEntrainement
    public void removeEntrainement(Entrainement entrainement){
        entrainements.remove(entrainement);
    }

    //Getters

    public List<Entrainement> getEntrainements() {
        return entrainements;
    }

    public ListeEntrainement getListeEntrainement() {
        return listeEntrainement;
    }
}
