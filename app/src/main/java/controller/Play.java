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
    private String strEntrainement;
    private String strSequence;
    private String strCycle;
    private String strTravail;
    private String strRepos;
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

        strEntrainement = getResources().getString(R.string.entrainement);
        strSequence = getResources().getString(R.string.sequence);
        strCycle = getResources().getString(R.string.cycle);
        strTravail = getResources().getString(R.string.travail);
        strRepos = getResources().getString(R.string.repos);
        if (extras != null){

            EntrainementAvecSequences entrainementAvecSequences = extras.getParcelable("entrainementAvecSequences");
            //entrainement = entrainementAvecSequences.getEntrainement();
            sequences = entrainementAvecSequences.getSequences();
            entrainement = extras.getParcelable("entrainement");

        }

        majTimer();
        TextView tview = findViewById(R.id.textView2);

    }

    public void getCycles(Sequence sequence) {

        class GetCyclesAsync extends android.os.AsyncTask<Void, Void, List<Cycle>>{


            @Override
            protected List<Cycle> doInBackground(Void... voids) {

                Long idCy = sequence.getSequenceId();
                    cycleIds = mDb
                            .sequenceCycleCrossRefDao()
                            .getCyclesId(sequence.getSequenceId());

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
        mapTimer.put(strEntrainement+space+entrainement.getNom(), tempsPrepa);
        for (int i = 0; i < sequences.size(); i++) {
            sequenceEnCours = sequences.get(i);
            getCycles(sequences.get(i));
        }
    }

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
            mapTimer.put(nomTravail+space+strTravail, tempsTravail);
            mapTimer.put(nomTravail+space+strRepos, tempsRepos);
            tempsTotal += tempsTravail;
            tempsTotal += tempsRepos;
        }

        String nomSequence = sequenceEnCours.getNom();
        int tempsReposLong = sequenceEnCours.getTempsReposLong();
        mapTimer.put(strSequence+space+nomSequence, tempsReposLong);
        tempsTotal += tempsReposLong;


    }


}