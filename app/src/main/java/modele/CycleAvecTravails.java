package modele;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CycleAvecTravails {
    @Embedded
    public Cycle cycle;

    @Relation(
            parentColumn = "CycleId",
            entityColumn = "travailId"
    )
    public List<Travail> travails;

    public CycleAvecTravails(Cycle cycle, List<Travail> travails){
        this.cycle = cycle;
        this.travails = travails;
    }

    public List<Travail> getTravails() {
        return travails;
    }

    public void addTravail(Travail travail){
        travails.add(travail);
    }

    public void removeTravail(Travail travail){
        travails.remove(travail);
    }
}
