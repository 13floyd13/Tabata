package modele;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class SequenceAvecCycles {
    @Embedded
    public Sequence sequence;

    //Relation de type one-to-many entre Sequence et Cycle
    @Relation(
            parentColumn = "sequenceId",
            entityColumn = "cycleId"
    )
    public List<Cycle> cycles;

    //Constructeur
    public SequenceAvecCycles(Sequence sequence, List<Cycle> cycles){
        this.sequence = sequence;
        this.cycles = cycles;
    }

    //Méthodes

    //Ajout d'un cycle à la séquence
    public void addCycle(Cycle cycle){
        cycles.add(cycle);
    }

    //suppression d'un cycle de la séquence
    public void removeCycle(Cycle cycle){
        cycles.remove(cycle);
    }

    //Getters
    public Sequence getSequence() {
        return sequence;
    }

    public List<Cycle> getCycles() {
        return cycles;
    }


}
