package controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.tabata.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import data.AppDatabase;
import data.DatabaseClient;
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
    private int tempsTotal = 0;
    private HashMap<String, Integer> mapTimer = new HashMap<>();
    private String travail = getResources().getString(R.string.travail);
    private String repos = getResources().getString(R.string.repos);
    private String space = " ";
    private Sequence sequenceEnCours;
    private Cycle cycleEnCours;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
        Bundle extras = getIntent().getExtras();
        if (extras != null){

            EntrainementAvecSequences entrainementAvecSequences = extras.getParcelable("entrainementAvecSequences");
            entrainement = entrainementAvecSequences.getEntrainement();
            sequences = entrainementAvecSequences.getSequences();

        }

        majTimer();
        TextView tview = findViewById(R.id.textView2);

    }

    public void getCycles(Sequence sequence) {

        class GetCyclesAsync extends android.os.AsyncTask<Void, Void, List<Cycle>>{

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
            protected List<Cycle> doInBackground(Void... voids) {
                //for (int i = 0; i < sequences.length; i++) {
                Long idCy = sequence.getSequenceId();
                    cycleIds = mDb
                            .sequenceCycleCrossRefDao()
                            .getCyclesId(sequence.getSequenceId());
                            //.getCyclesId(sequences[i].getSequenceId());
                //}
                List<Cycle> listCycles = mDb
                        .cycleDao()
                        .getCycles(cycleIds);

                cycles = listCycles;
                return listCycles;
            }

            @Override
            protected void onPostExecute(List<Cycle> listCycles){
                super.onPostExecute(listCycles);

                //Mise à jour de l'adapter avec la liste d'entrainements
                cycles = listCycles;
                majCycle(listCycles);
            }
        }
        GetCyclesAsync getCyclesAsync = new GetCyclesAsync();
        getCyclesAsync.execute();
    }

    public void getTravails(Cycle cycle){

        class GetTravailsAsync extends android.os.AsyncTask<Void, Void, List<Travail>>{

            @Override
            protected List<Travail> doInBackground(Void... voids) {
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

            @Override
            protected void onPostExecute(List<Travail> listTravails){
                super.onPostExecute(listTravails);

                //Mise à jour de l'adapter avec la liste d'entrainements
                travails = listTravails;
                majTravail(listTravails);
            }
        }
        GetTravailsAsync getTravailsAsync = new GetTravailsAsync();
        getTravailsAsync.execute();
    }

    public void majTimer() {

        int tempsPrepa = entrainement.getTempsPreparation();
        tempsTotal += tempsPrepa;
        mapTimer.put(entrainement.getNom(), tempsPrepa);
        for (int i = 0; i < sequences.size(); i++) {
            sequenceEnCours = sequences.get(i);
            getCycles(sequences.get(i));

           /* List<Long> cy = cycleIds;
            //for (int j = 0; j < cycles.size(); j++){
                //getTravails(cycles.get(j));

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
            tempsTotal += tempsReposLong;*/
        }
    }



        /*for sequences
                timer.add temps prepa
                get cycles
                        for cycles
                get travails
                        for travails
                timer.add travail 1
                timer.add travail 2*/



    public void majCycle(List<Cycle> lcycles){

        for (int j = 0; j < lcycles.size(); j++){
            cycleEnCours = lcycles.get(j);
            getTravails(lcycles.get(j));
        }
    }

    public void majTravail(List<Travail> ltravails){
        for (int i = 0; i < ltravails.size(); i++){
            String nomTravail = travails.get(i).getNom();
            int tempsTravail = travails.get(i).getTemps();
            int tempsRepos = travails.get(i).getRepos();
            mapTimer.put(nomTravail+space+travail, tempsTravail);
            mapTimer.put(nomTravail+space+repos, tempsRepos);
            tempsTotal += tempsTravail;
            tempsTotal += tempsRepos;
        }

        String nomSequence = sequenceEnCours.getNom();
        int tempsReposLong = sequenceEnCours.getTempsReposLong();
        mapTimer.put(nomSequence, tempsReposLong);
        tempsTotal += tempsReposLong;


    }


}