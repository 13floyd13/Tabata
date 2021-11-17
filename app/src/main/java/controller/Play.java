package controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.tabata.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import data.AppDatabase;
import modele.Cycle;
import modele.Entrainement;
import modele.EntrainementAvecSequences;
import modele.Sequence;
import modele.Travail;

public class Play extends AppCompatActivity {

    private AppDatabase mDb;
    private Entrainement entrainement;
    private List<Sequence> sequences;
    private List<Cycle> cycles;
    private List<Travail> travails;
    private List<Long> cycleIds;
    private List<Long> travailIds;
    private int tempsTotal;
    private HashMap<String, Integer> mapTimer = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Bundle extras = getIntent().getExtras();
        if (extras != null){

            EntrainementAvecSequences entrainementAvecSequences = extras.getParcelable("entrainementAvecSequences");
            entrainement = entrainementAvecSequences.getEntrainement();
            sequences = entrainementAvecSequences.getSequences();

        }
    }

    public void getCycles(Sequence sequence) {

        class GetCyclesAsync extends android.os.AsyncTask<Sequence, Void, List<Cycle>>{

            /*@Override
            protected List<Cycle> doInBackground(Void... voids) {
                for (int i = 0; i < sequences.size(); i++){
                    List<Long> ids = mDb
                            .sequenceCycleCrossRefDao()
                            .getCyclesId(sequences.get(i).getSequenceId());

                    cycleIds.addAll(ids);
                }

                cycles = mDb
                        .cycleDao()
                        .getCycles(cycleIds);

                return cycles;

            }*/

            @Override
            protected List<Cycle> doInBackground(Sequence... sequences) {
                //for (int i = 0; i < sequences.length; i++) {
                    cycleIds = mDb
                            .sequenceCycleCrossRefDao().getCyclesId(sequence.getSequenceId());
                            //.getCyclesId(sequences[i].getSequenceId());
                //}
                cycles = mDb
                        .cycleDao()
                        .getCycles(cycleIds);

                return cycles;
            }
        }
        GetCyclesAsync getCyclesAsync = new GetCyclesAsync();
        getCyclesAsync.execute();
    }

    public void getTravails(Cycle cycle){

        class GetTravailsAsync extends android.os.AsyncTask<List<Long>, Void, List<Travail>>{

            @Override
            protected List<Travail> doInBackground(List<Long>... lists) {
                /*for (int i = 0; i < lists.length; i++){
                    List<Long> ids = mDb
                            .cycleTravailCrossRefDao()
                            .getTravailIds(lists[i]);
                    travailIds.addAll(ids);
                }*/
                travailIds = mDb.cycleTravailCrossRefDao().getTravailIds(cycle.getCycleId());

                travails = mDb
                        .travailDao()
                        .getTravails(travailIds);
                return travails;
            }
        }
        GetTravailsAsync getTravailsAsync = new GetTravailsAsync();
        getTravailsAsync.execute();
    }

    public void majTimer(){

        int tempsPrepa = entrainement.getTempsPreparation();
        tempsTotal += tempsPrepa;
        mapTimer.put(entrainement.getNom(), tempsPrepa);
        for (int i = 0; i < sequences.size(); i++){
            getCycles(sequences.get(i));

            for (int j = 0; j < cycles.size(); i++){
                getTravails(cycles.get(i));

                for (int k = 0; k < travails.size(); i++){
                    String nomTravail = travails.get(i).getNom();
                    String space = " ";
                    String travail = getResources().getString(R.string.travail);
                    String repos = getResources().getString(R.string.repos);
                    int tempsTravail = travails.get(i).getTemps();
                    int tempsRepos = travails.get(i).getRepos();
                    mapTimer.put(nomTravail+space+travail, tempsTravail);
                    mapTimer.put(nomTravail+space+repos, tempsRepos);
                    tempsTotal += tempsTravail;
                    tempsTotal += tempsRepos;
                }
                String nomCycle = cycles.get(i).getNom();
            }
            String nomSequence = sequences.get(i).getNom();
            int tempsReposLong = sequences.get(i).getTempsReposLong();
            mapTimer.put(nomSequence, tempsReposLong);
            tempsTotal += tempsReposLong;
        }

        /*for sequences
                timer.add temps prepa
                get cycles
                        for cycles
                get travails
                        for travails
                timer.add travail 1
                timer.add travail 2*/



    }
}