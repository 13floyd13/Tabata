package controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tabata.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import data.AppDatabase;
import data.DatabaseClient;
import modele.Compteur;
import modele.Cycle;
import modele.Entrainement;
import modele.EntrainementAvecSequences;
import modele.Sequence;
import modele.Travail;
import modele.OnUpdateListener;

public class Play extends AppCompatActivity implements OnUpdateListener {

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
    private String strTempsReposLong;
    private String strTempsPreparation;
    private Sequence sequenceEnCours;
    private Cycle cycleEnCours;
    private ArrayList<Compteur> compteurs = new ArrayList<>();
    TextView tvNomEntrainement;
    TextView tvTimerTotal;
    TextView tvNomSequence;
    TextView tvNomCycle;
    TextView tvNomTravail;
    TextView tvTimer;
    Button btnPause;
    Button btnSuivant;
    private Compteur compteurTravailEnCours;
    private Compteur compteurTempsTotal;
    private boolean start;
    private String strStart;
    private String strPause;
    private Play activity;
    private boolean go = false;



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
        strTempsReposLong = getResources().getString(R.string.reposLong);
        strTempsPreparation = getResources().getString(R.string.preparation);
        strStart = getResources().getString(R.string.start);
        strPause = getResources().getString(R.string.pause);

        if (extras != null){

            EntrainementAvecSequences entrainementAvecSequences = extras.getParcelable("entrainementAvecSequences");
            //entrainement = entrainementAvecSequences.getEntrainement();
            sequences = entrainementAvecSequences.getSequences();
            entrainement = extras.getParcelable("entrainement");

        }

        majTimer();

        tvNomEntrainement = findViewById(R.id.nomEntrainement);
        tvTimerTotal = findViewById(R.id.tempsTotal);
        tvNomSequence = findViewById(R.id.nomSequence);
        tvNomCycle = findViewById(R.id.nomCycle);
        tvNomTravail = findViewById(R.id.nomTravail);
        tvTimer = findViewById(R.id.timer);
        btnPause = findViewById(R.id.pause);
        btnSuivant = findViewById(R.id.suivant);
        tvNomEntrainement.setText(entrainement.getNom());


        this.activity = this;
        AlertDialog.Builder popup = new AlertDialog.Builder(activity);
        popup.setTitle("Entrainement");
        popup.setMessage("Lancer l'entrainement");
        popup.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                go = true;
                checkStart();


            }
        });

        popup.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        popup.show();





       // }

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
        Compteur tempsPrepaCompteur = new Compteur(tempsPrepa);
        tempsPrepaCompteur.setNomTravail(strTempsPreparation);
        compteurs.add(tempsPrepaCompteur);
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
            Compteur travailCompteur = new Compteur(tempsTravail);
            travailCompteur.setNomSequence(sequenceEnCours.getNom());
            travailCompteur.setNomCycle(cycleEnCours.getNom());
            travailCompteur.setNomTravail(nomTravail);
            compteurs.add(travailCompteur);

            Compteur reposCompteur = new Compteur(tempsRepos);
            reposCompteur.setNomSequence(sequenceEnCours.getNom());
            reposCompteur.setNomCycle(cycleEnCours.getNom());
            reposCompteur.setNomTravail(strRepos);
            compteurs.add(reposCompteur);

        }

        String nomSequence = sequenceEnCours.getNom();
        int tempsReposLong = sequenceEnCours.getTempsReposLong();
        mapTimer.put(strSequence+space+nomSequence, tempsReposLong);
        tempsTotal += tempsReposLong;

        Compteur reposLongCompteur = new Compteur(tempsReposLong);
        reposLongCompteur.setNomSequence(sequenceEnCours.getNom());
        reposLongCompteur.setNomCycle(cycleEnCours.getNom());
        reposLongCompteur.setNomTravail(strTempsReposLong);
        compteurs.add(reposLongCompteur);


    }

    public void lancerEntrainement(){

        compteurTempsTotal = new Compteur(tempsTotal);

        for (int i = 0; i < compteurs.size(); i++){
            compteurTravailEnCours= compteurs.get(i);


                tvNomSequence.setText(compteurTravailEnCours.getNomSequence());
                tvNomCycle.setText(compteurTravailEnCours.getNomCycle());
                tvNomTravail.setText(compteurTravailEnCours.getNomTravail());
                compteurTravailEnCours.addOnUpdateListener(this);

                majCompteur();
                //compteurTempsTotal.start();
                //compteurTravailEnCours.start();
                //start = true;
                btnPause.setText(strStart);


        }
    }

    private void majCompteur() {
        // Affichage des informations du compteur
        tvTimer.setText("" + compteurTravailEnCours.getMinutes() + ":"
                + String.format("%02d", compteurTravailEnCours.getSecondes()) + ":"
                + String.format("%03d", compteurTravailEnCours.getMillisecondes()));

        tvTimerTotal.setText("" + compteurTempsTotal.getMinutes() + ":"
                + String.format("%02d", compteurTempsTotal.getSecondes()) + ":"
                + String.format("%03d", compteurTempsTotal.getMillisecondes()));
    }


    @Override
    public void onUpdate() {
        majCompteur();
    }

    public void onPause(View view) {
        if (start) {
            compteurTravailEnCours.pause();
            compteurTempsTotal.pause();
            start = false;
            btnPause.setText(strStart);
        }else {
            compteurTravailEnCours.start();
            compteurTempsTotal.start();
            start = true;
            btnPause.setText(strPause);
        }
    }

    public void checkStart(){
        if (go){
            lancerEntrainement();
        }
    }
}