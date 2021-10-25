package modele;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class EntrainementAvecSequences {
    @Embedded
    public Entrainement entrainement;

    //Relation de type one-to-many entre Entrainement et Sequence
    @Relation(
            parentColumn = "entrainementId",
            entityColumn = "sequenceId"
    )
    public List<Sequence> sequences ;

    //Constructeur
    public EntrainementAvecSequences(Entrainement entrainement, List<Sequence> sequences){
        this.entrainement = entrainement;
        this.sequences = sequences;
    }

    //Méthodes

    //Ajout d'une séquence à un entrainement
    public void addSequence(Sequence sequence){
        sequences.add(sequence);
    }

    //suppression d'une séquence de l'entrainement
    public void removeSequence(Sequence sequence){
        sequences.remove(sequence);
    }

    //Getters

    public Entrainement getEntrainement() {
        return entrainement;
    }

    public List<Sequence> getSequences() {
        return sequences;
    }
}
