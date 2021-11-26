package controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tabata.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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

    //Data
    private AppDatabase mDb;

    //Attributs
    private Entrainement entrainement;
    private List<Sequence> sequences;
    private List<Cycle> cycles;
    private List<Travail> travails;
    private List<Long> cycleIds;
    private List<Long> travailIds;
    private int tempsTotal = 0;
    private Sequence sequenceEnCours;
    private Cycle cycleEnCours;
    private ArrayList<Compteur> compteurs = new ArrayList<>();
    private Compteur compteurTravailEnCours;
    private Compteur compteurTempsTotal;
    private boolean start;
    private boolean go = false;
    private int iterateurCompteur = 0;
    private ToneGenerator bip;
    private Date date;

    //Views
    private TextView tvNomEntrainement;
    private TextView tvTimerTotal;
    private TextView tvNomSequence;
    private TextView tvNomCycle;
    private TextView tvNomTravail;
    private TextView tvTimer;
    private TextView tvProchainTravail;
    private Button btnPause;
    private Button btnSuivant;
    private Play activity;

    //Ressources
    private String strTempsReposLong;
    private String strTempsPreparation;
    private String strProchainTravail;
    private String strFin;
    private String strSuivant;
    private String strRepos;
    private String strStart;
    private String strPause;
    private String strOui;
    private String strNon;
    private String strLancer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();


        //récupération des strings en ressources
        strRepos = getResources().getString(R.string.repos);
        strTempsReposLong = getResources().getString(R.string.reposLong);
        strTempsPreparation = getResources().getString(R.string.preparation);
        strStart = getResources().getString(R.string.start);
        strPause = getResources().getString(R.string.pause);
        strProchainTravail = getResources().getString(R.string.prochain);
        strFin = getResources().getString(R.string.fin);
        strSuivant = getResources().getString(R.string.suivant);
        strOui = getResources().getString(R.string.oui);
        strNon = getResources().getString(R.string.non);
        strLancer = getResources().getString(R.string.lancer);

        //récupération de l'entrainement et des sequences
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            EntrainementAvecSequences entrainementAvecSequences = extras.getParcelable("entrainementAvecSequences");
            //sequences = entrainementAvecSequences.getSequences();
            sequences = extras.getParcelableArrayList("sequences_key");
            entrainement = extras.getParcelable("entrainement");
        }

        //récupération de toutes les données en base de donnée
        majTimer();

        //récupération des views
        tvNomEntrainement = findViewById(R.id.nomEntrainement);
        tvTimerTotal = findViewById(R.id.tempsTotal);
        tvNomSequence = findViewById(R.id.nomSequence);
        tvNomCycle = findViewById(R.id.nomCycle);
        tvNomTravail = findViewById(R.id.nomTravail);
        tvTimer = findViewById(R.id.timer);
        tvProchainTravail = findViewById(R.id.prochainTravail);
        btnPause = findViewById(R.id.pause);
        btnSuivant = findViewById(R.id.suivant);
        tvNomEntrainement.setText(entrainement.getNom());

        //initialisation du bip
        bip = new ToneGenerator(AudioManager.STREAM_NOTIFICATION,100);


        //mise en place d'une popup pour demander si on lance l'entrainement
        this.activity = this;
        AlertDialog.Builder popup = new AlertDialog.Builder(activity);
        popup.setTitle(entrainement.getNom());
        popup.setMessage(strLancer);

        //Bouton oui cliquable
        popup.setPositiveButton(strOui, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //fermeture de la boite de dialogue
                go = true;
                checkStart(); //lancement de l'entrainement
            }
        });

        //bouton non cliquable
        popup.setNegativeButton(strNon, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        //lancement de la popup
        popup.show();

    }

    //récupération des cycles pour une séquence
    public void getCycles(Sequence sequence) {

        class GetCyclesAsync extends android.os.AsyncTask<Void, Void, List<Cycle>>{

            int nbRepetitions;

            @Override
            protected List<Cycle> doInBackground(Void... voids) {

            nbRepetitions = mDb
                    .sequenceDao()
                    .getRepetitions(sequence.getSequenceId());


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

                cycles = listCycles;
                majCycle(listCycles, nbRepetitions);
            }
        }
        GetCyclesAsync getCyclesAsync = new GetCyclesAsync();
        getCyclesAsync.execute();
    }

    //récupération des travails pour un cycle
    public void getTravails(Cycle cycle){

        int nbRepetitionsCycles= cycle.getRepetition();
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

                travails = listTravails;
                majTravail(listTravails, nbRepetitionsCycles);
            }
        }
        GetTravailsAsync getTravailsAsync = new GetTravailsAsync();
        getTravailsAsync.execute();
    }

    //Lance la boucle pour récupérer tous les timers et les mettre dans une list de Compteur
    public void majTimer() {

        int tempsPrepa = entrainement.getTempsPreparation();
        tempsTotal += tempsPrepa;

        Compteur tempsPrepaCompteur = new Compteur(tempsPrepa);
        tempsPrepaCompteur.setNomTravail(strTempsPreparation);
        compteurs.add(tempsPrepaCompteur);

        //on débute la boucle sur les séquences
        for (int i = 0; i < sequences.size(); i++) {
            sequenceEnCours = sequences.get(i);

            //récupération des cycles pour chaque séquence
            getCycles(sequences.get(i));
        }
    }

    //méthode pour appeler les travails pour chaque cycle
    public void majCycle(List<Cycle> lcycles, int nbRepetitionsSequence){

        //boucle dépendant du nombre de répétitions d'une séquence
        for (int i = 0; i < nbRepetitionsSequence; i++) {

            //boucle sur l'ensemble des cycles de la dernière séquence récupéré
            for (int j = 0; j < lcycles.size(); j++) {
                cycleEnCours = lcycles.get(j);

                //récupérations des travails pour chaque cycle
                getTravails(lcycles.get(j));
            }
        }
    }

    //méthode pour créer des compteurs pour chaque travail
    public void majTravail(List<Travail> ltravails, int nbRepetitionsCycle){

        //boucle sur le nombre de répétition du dernier cycle récupéré
        for (int j = 0; j < nbRepetitionsCycle; j++) {

            //boucle sur les travails du dernier cycle récupéré
            for (int i = 0; i < ltravails.size(); i++) {

                //récupération des informations du travail
                String nomTravail = travails.get(i).getNom();
                int tempsTravail = travails.get(i).getTemps();
                int tempsRepos = travails.get(i).getRepos();

                tempsTotal += tempsTravail;
                tempsTotal += tempsRepos;

                //création du compteur pour ce travail
                Compteur travailCompteur = new Compteur(tempsTravail);
                travailCompteur.setNomSequence(sequenceEnCours.getNom());
                travailCompteur.setNomCycle(cycleEnCours.getNom());
                travailCompteur.setNomTravail(nomTravail);
                //ajout à la liste de compteur
                compteurs.add(travailCompteur);

                //création du compteur pour ce repos
                Compteur reposCompteur = new Compteur(tempsRepos);
                reposCompteur.setNomSequence(sequenceEnCours.getNom());
                reposCompteur.setNomCycle(cycleEnCours.getNom());
                reposCompteur.setNomTravail(strRepos);
                //ajout à la liste de compteur
                compteurs.add(reposCompteur);

            }
        }


        //récupération du temps de repos de la séquence qui intervient après tous les travails et repos de cette même séquence
        int tempsReposLong = sequenceEnCours.getTempsReposLong();

        tempsTotal += tempsReposLong;

        //création du compteur de repos long pour la séquence en cours
        Compteur reposLongCompteur = new Compteur(tempsReposLong);
        reposLongCompteur.setNomSequence(sequenceEnCours.getNom());
        reposLongCompteur.setNomCycle(cycleEnCours.getNom());
        reposLongCompteur.setNomTravail(strTempsReposLong);
        compteurs.add(reposLongCompteur);


    }

    //methode pour jouer tous les compteurs
    //on ne fait pas une boucle car il faut attendre que chaque compteur soit terminé pour passer au suivant
    //Lorsque le compteur est terminé il envoie une info qui est récupéré par le listener
    //quand l'info est récup on relance cette méthode qui va itérer sur le prochain compteur
    public void lancerEntrainement(){
        //on récupère l'itération
        int i = iterateurCompteur;

        //on vérifie que le compteur existe sinon c'est la fin de l'entrainement
        if( i < compteurs.size()){
            compteurTravailEnCours= compteurs.get(i);

            //on vérifie qu'il existe qu'un prochain compteur existe sinon c'est la fin de l'entrainement
            if((i+1) < compteurs.size()){

                //on créer un compteur suivaant pour récupérer son  nom de Travail et pour l'afficher sur le layout
                Compteur compteurSuivant = compteurs.get(i+1);
                tvProchainTravail.setText(strProchainTravail+compteurSuivant.getNomTravail());
                btnSuivant.setText(strSuivant);

            }else{

                //c'est la fin de l'entrainement
                tvProchainTravail.setText(strProchainTravail+strFin);
                btnSuivant.setText(strFin);

                //on change la propriété onClick du bouton pour envoyer vers la fin de l'entrainement
                btnSuivant.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finEntrainement();
                    }
                });
            }

            //on met à jour le design en fonction du compteur en cours
            majDesign();

            //on met à jour le layout avec les infos du compteur
            tvNomSequence.setText(compteurTravailEnCours.getNomSequence());
            tvNomSequence.setTextColor(Color.WHITE);
            tvNomCycle.setText(compteurTravailEnCours.getNomCycle());
            tvNomTravail.setText(compteurTravailEnCours.getNomTravail());

            //on lie le compteur à l'interface
            compteurTravailEnCours.addOnUpdateListener(this);

            //on démarre le compteur
            compteurTravailEnCours.start();

            //boolean pour gérer le design du bouton start/pause
            start = true;
            btnPause.setText(strPause);

            //mise à jour du visuel du timer
            majCompteur();


        }else{
            //fin de l'entrainement
            finEntrainement();
        }

    }

    private void majCompteur() {
        // Affichage des informations du compteur
        tvTimer.setText("" + compteurTravailEnCours.getMinutes() + ":"
                + String.format("%02d", compteurTravailEnCours.getSecondes())); /*+ ":"
                + String.format("%03d", compteurTravailEnCours.getMillisecondes()));*/

        tvTimerTotal.setText("" + compteurTempsTotal.getMinutes() + ":"
                + String.format("%02d", compteurTempsTotal.getSecondes()));/* + ":"
                + String.format("%03d", compteurTempsTotal.getMillisecondes()));*/
    }


    //methode appelé en continue pendant le start du timer pour mettre à jour le visuel
    @Override
    public void onUpdate() {
        majCompteur();

        //si on est dans les trois dernières secondes on joue un bip
        if (compteurTravailEnCours.getUpdatedTime() < 3050 && compteurTravailEnCours.getUpdatedTime() > 2950 /*|| compteurTravailEnCours.getSecondes() == 2 || compteurTravailEnCours.getSecondes() == 1 */){
            bip.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
        }else if (compteurTravailEnCours.getUpdatedTime() < 2050 && compteurTravailEnCours.getUpdatedTime() > 1950){
            bip.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
        }else if (compteurTravailEnCours.getUpdatedTime() < 1050 && compteurTravailEnCours.getUpdatedTime() > 950){
            bip.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
        }


    }

    //methode appelé lorsque que le timer est terminé ou lorsque l'on presse le bouton suivant pour passer au prochain timer
    @Override
    public void onFinish() {

        //Arret du compteur
        if (compteurTempsTotal.getTimer() != null){
            compteurTempsTotal.stop();
        }

        //iréation pour passer au prochain compteur
        iterateurCompteur++;

        if(start == false){
            compteurTempsTotal.start();
        }
        //lancement du Compteur
        lancerEntrainement();
    }

    //méthode appelé lorsque qu'on met en Pause le timer en cours
    //cette méthode met aussi en pause le compteur total de l'entrainement
    public void onPause(View view) {

        if (start) {

            compteurTravailEnCours.pause();
            compteurTempsTotal.pause();
            start = false;

            //changement de nom du bouton
            btnPause.setText(strStart);

        }else {

            compteurTravailEnCours.start();
            compteurTempsTotal.start();
            start = true;

            //changement de nom du bouton
            btnPause.setText(strPause);

        }
    }

    //methode qui lance la méthode pour le premier compteur lorsque l'utilisateur dit oui sur la popup
    public void checkStart(){
        if (go){
            compteurTempsTotal = new Compteur(tempsTotal);
            compteurTempsTotal.start();
            lancerEntrainement();
        }
    }

    //Méthode pour mettre à jour le design en fonction du compteur
    //Couleur pour le temps de préparation : vert
    // pour le repos long : bleu
    // pour le repos : cyan
    //pour le travail : rouge
    public void majDesign(){

        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.screen);

        if (compteurTravailEnCours.getNomTravail() == strTempsPreparation){
            constraintLayout.setBackgroundColor(Color.GREEN);
        }else if (compteurTravailEnCours.getNomTravail() == strTempsReposLong){
            constraintLayout.setBackgroundColor(Color.BLUE);
        }else if (compteurTravailEnCours.getNomTravail() == strRepos){
            constraintLayout.setBackgroundColor(Color.CYAN);
        }else{
            constraintLayout.setBackgroundColor(Color.RED);
        }

    }

    //méthode appelé lorsqu'on souhaite pazsser au prochain timer en cours de lancement
    public void onNext(View view) {

        //vérification si une itération supplémentaire est possible sinon fin de l'entrainement
        if((iterateurCompteur +1) < compteurs.size()){

            //on récupère le travail restant du timer
            long resteTimeTravail = compteurTravailEnCours.getUpdatedTime();

            //on récupère le temps restant du compteur de temps total de l'entrainement
            long resteTotal = compteurTempsTotal.getUpdatedTime();

            //on stop le timer temps total
            if (compteurTempsTotal.getTimer() != null){
                compteurTempsTotal.stop();
            }

            if (compteurTravailEnCours.getTimer() != null){
                compteurTravailEnCours.stop();
            }


            //on créer un nouveau compteur avec la soustraction du temps passé par l'utilisateur
            compteurTempsTotal = new Compteur((resteTotal - resteTimeTravail)/1000);

            //on démarre ce timer
            compteurTempsTotal.start();

            //on appel la méthode de fin du timer de travail
            onFinish();

        }else{
            finEntrainement();
        }

    }

    public void finEntrainement(){

        //Arret des compteurs
        //on vérifie si le compteur a déja été stop
        if (compteurTravailEnCours.getTimer() != null && start == true){
            compteurTravailEnCours.stop();
        }

        if(compteurTempsTotal.getTimer() != null && start == true){
            compteurTempsTotal.stop();
        }


        //récupération de la date du jour
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        Intent goToFinEntrainement = new Intent(getApplicationContext(), FinEntrainement.class);
        goToFinEntrainement.putExtra("currentDate", currentDate);
        goToFinEntrainement.putExtra("nomEntrainement", entrainement.getNom());
        startActivity(goToFinEntrainement);
    }
}