package modele;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CycleAvecTravails {
    @Embedded
    public Cycle cycle;

    //Relation de type one-to-many entre cycle et travail
    @Relation(
            parentColumn = "cycleId",
            entityColumn = "travailId"
    )
    public List<Travail> travails;

    //Constructeur
    public CycleAvecTravails(Cycle cycle, List<Travail> travails){
        this.cycle = cycle;
        this.travails = travails;
    }

    //Méthodes

    //Ajout d'un travail au cycle
    public void addTravail(Travail travail){
        travails.add(travail);
    }

    //Suppression d'un travail du cycle
    public void removeTravail(Travail travail){
        travails.remove(travail);
    }

    //Getters
    public List<Travail> getTravails() {
        return travails;
    }

    public Cycle getCycle() {
        return cycle;
    }


}
