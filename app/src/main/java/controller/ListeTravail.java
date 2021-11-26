package controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabata.R;

import java.util.ArrayList;
import java.util.List;

import data.AppDatabase;
import data.DatabaseClient;
import modele.Cycle;
import modele.Entrainement;
import modele.Sequence;
import modele.Travail;
import modele.TravailListAdapter;

public class ListeTravail extends AppCompatActivity {


    //Data
    private AppDatabase mDb;
    private TravailListAdapter adapter;

    //Attributs
    private ArrayList<Travail> travails = new ArrayList<Travail>();
    private boolean suppression = false;
    private ArrayList<Long> travailsAjoutes = new ArrayList<>();
    private String nomCycle;
    private String nbRepet;
    private ArrayList<Cycle> cyclesAsupprimer = new ArrayList<>();
    private ArrayList<Sequence> sequencesAsupprimer = new ArrayList<>();
    private ArrayList<Entrainement> entrainementAsupprimer = new ArrayList<>();
    private ListeTravail activity;
    private AlertDialog.Builder popup1;
    private AlertDialog.Builder popup2;
    private Travail travailClicked;


    //Views
    private ListView listTravail;
    private TextView titrePage;

    //Ressources
    private String liste;
    private String space;
    private String travail;
    private String strSuppression;
    private String strsuppressionTravail;
    private String strSuppressionSupplementaire;
    private String oui;
    private String non;
    private String strCycle;
    private String strSequence;
    private String strEntrainement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_travail);

        this.activity = this;

        //préparation de la popup
        popup1 = new AlertDialog.Builder(activity);
        popup2 = new AlertDialog.Builder(activity);

        //récupération des intents
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            travails = extras.getParcelableArrayList("arrayListTravails");
            suppression = extras.getBoolean("SUPPRESSION_KEY");
            nomCycle = extras.getString("nomCycle");
            nbRepet = extras.getString("nbRepet");
        }

        //On récupère des strings en ressources à concatener
        strSuppression = getResources().getString(R.string.suppression);
        strsuppressionTravail = getResources().getString(R.string.suppressionTravail);
        strSuppressionSupplementaire = getResources().getString(R.string.suppressionSupplementaire);
        strCycle = getResources().getString(R.string.cycle);
        strSequence = getResources().getString(R.string.sequence);
        strEntrainement = getResources().getString(R.string.entrainement);
        oui = getResources().getString(R.string.oui);
        non = getResources().getString(R.string.non);
        liste = getResources().getString(R.string.liste);
        space = " ";
        travail = getResources().getString(R.string.travail);
        String strListeTravail = liste + space + travail;

        //récupération du TextView de temps de travail pour ajouter la string
        titrePage = findViewById(R.id.titrePage);
        titrePage.setText(strListeTravail);

        //récupération du ListView
        listTravail = findViewById(R.id.listTravails);

        // Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();

        //Liaison de l'adapder au listView
        adapter = new TravailListAdapter(this, new ArrayList<Travail>());
        listTravail.setAdapter(adapter);

        //si on vient du menu Supression
        if (suppression) {

            listTravail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //récupération du travail cliqué
                    travailClicked = adapter.getItem(position);

                    //récupération des cycles qui contiennent ce travail
                    getCyclesAssociated();

                    //mise en place de la popup de validation de la suppression de ce travail
                    popup1.setTitle(strSuppression);
                    popup1.setMessage(strsuppressionTravail + space + travailClicked.getNom());

                    popup1.setPositiveButton(oui, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //on regarde si on aura des cycles qui deviendront vides et devront donc être supprimés
                            //cela demandera une deuxième validation à l'utilisateur
                            if(!cyclesAsupprimer.isEmpty()){
                                askSuppression();

                                //sinon on supprime juste le travail
                            }else{
                                suppression();
                            }

                            //fermeture de la popup
                            dialog.dismiss();
                        }
                    });

                    popup1.setNegativeButton(non, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //si non on ferme juste l'activité
                            finish();
                        }
                    });

                    //affichage de la popup
                    popup1.show();
                }
            });

            //si on vient du menu Creation
        }else {

            //ajout d'un évenement click à la listeView
            listTravail.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //Récupération du travail cliqué pour l'envoyer à la création du cycle dans un arayList de travail
                    Travail travailClicked = adapter.getItem(position);

                    //boucle pour récuperer les id des travails ajoutés à la creation de cycle
                    for (int i = 0; i < travails.size(); i++) {
                        travailsAjoutes.add(travails.get(i).getTravailId());
                    }

                    //on vérifie que le travail n'a pas déja été cliqué
                    if (travailsAjoutes.contains(travailClicked.getTravailId())) {

                        Toast toast = Toast.makeText(ListeTravail.this, "Sequence déja ajouté", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER, 20, 30);
                        toast.show();

                        //sinon on ajoute le travail à la liste de travails déja cliqué avant et on envoie à l'activity de creation de cycle
                    } else {

                        Intent goBacktoCycle = new Intent(getApplicationContext(), CreationCycle.class);
                        travails.add(travailClicked);
                        goBacktoCycle.putParcelableArrayListExtra("arrayListTravailsClicked", travails);
                        goBacktoCycle.putExtra("nomCycle", nomCycle);
                        goBacktoCycle.putExtra("nbRepet", nbRepet);
                        goBacktoCycle.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(goBacktoCycle);
                    }
                }
            });
        }
    }

    //récupération des travails en bd
    private void getTravails() {

        class RecupererTravailAsync extends android.os.AsyncTask<Void, Void, List<Travail>> {

            @Override
            protected List<Travail> doInBackground(Void... voids) {
                List<Travail> travailList = mDb
                        .travailDao()
                        .getAll();
                return travailList;
            }

            @Override
            protected void onPostExecute(List<Travail> travails){
                super.onPostExecute(travails);

                //Mise à jour de l'adapteur avec la liste des travails
                adapter.clear();
                adapter.addAll(travails);
                adapter.notifyDataSetChanged();
            }
        }

        //execution de la demande asynchrone
        RecupererTravailAsync recup = new RecupererTravailAsync();
        recup.execute();
    }

    //récupération des  id des cycles puis des cycles qui contiennent le travail cliqué
    public void getCyclesAssociated(){

        class RecupererCyclesAssociesAsync extends android.os.AsyncTask<Void, Void, List<Cycle>>{

            @Override
            protected List<Cycle> doInBackground(Void... voids) {

                List<Long> idsCycle = mDb
                        .cycleTravailCrossRefDao()
                        .getCycleIds(travailClicked.getTravailId());


                List<Cycle> cycles1 = mDb
                        .cycleDao()
                        .getCycles(idsCycle);
                return cycles1;

            }

            @Override
            protected void onPostExecute(List<Cycle> cyclesACheck){
                super.onPostExecute(cyclesACheck);

                //Pour chaque cycle qui contient le travail à supprimer
                //on va vérifier si ce cycle ne contient que ce travail
                //dans ce cas il faudra le supprimer
                for (int i = 0; i < cyclesACheck.size(); i++){
                    getNumberTravails(cyclesACheck.get(i));
                }
            }
        }

        RecupererCyclesAssociesAsync recupererCyclesAssociesAsync = new RecupererCyclesAssociesAsync();
        recupererCyclesAssociesAsync.execute();

    }

    //récupération des cycles ne contenant que le travail cliqué
    //s'ils contiennent que le travail cliqué on doit les supprimer pour ne pas avoir de cycle vide dans l'application
    //on les ajoutes donc dans une liste de cycles à supprimer
    public void getNumberTravails(Cycle cycleACheck){

        class RecupererCyclesADelete extends android.os.AsyncTask<Void, Void, Integer>{

            @Override
            protected Integer doInBackground(Void... voids) {

                int nbTravails = mDb
                        .cycleTravailCrossRefDao()
                        .getNbTravails(cycleACheck.getCycleId());

                return nbTravails;
            }

            @Override
            protected void onPostExecute(Integer nbTravails){
                super.onPostExecute(nbTravails);

                //Si le nombre de travail ==1 c'est que le cycle ne contient que le travail à supprimer
                //on ajoute donc le cycle en question à la liste de cycles à supprimer
                if (nbTravails == 1){
                    cyclesAsupprimer.add(cycleACheck);

                    //On va vérifier de la même façon toutes les sequences qui contiennent ce cycle qui sera supprimé
                    getSequenceAssocies(cycleACheck);
                }
            }
        }

        RecupererCyclesADelete recupererCyclesADelete = new RecupererCyclesADelete();
        recupererCyclesADelete.execute();

    }

    //on récupère toutes les sequences contenant le cycle qui sera supprimé
    public void getSequenceAssocies(Cycle cycleQuiSeraDelete){

        class RecupererSequenceAssocies extends android.os.AsyncTask<Void, Void, List<Sequence>>{

            @Override
            protected List<Sequence> doInBackground(Void... voids) {
                List<Long> sequencesIds = mDb
                        .sequenceCycleCrossRefDao()
                        .getSequencesId(cycleQuiSeraDelete.getCycleId());

                List<Sequence> sequences = mDb
                        .sequenceDao()
                        .getSequences(sequencesIds);
                return sequences;
            }

            @Override
            protected void onPostExecute(List<Sequence> sequences) {
                super.onPostExecute(sequences);

                for (int i = 0; i < sequences.size(); i++){
                    //on va vérifier chaque séquence s'il elle contient suelement le cycle à supprimer ou non
                    checkSequence(sequences.get(i));
                }
            }

        }

        RecupererSequenceAssocies recupererSequenceAssocies = new RecupererSequenceAssocies();
        recupererSequenceAssocies.execute();

    }

    //on va regarder le nombre de cycle dans cette sequence
    //si un seul cycle, ce cycle sera celui qui sera supprimé
    //il faudra donc ajouter la sequence dans une list de sequence à supprimer
    public void checkSequence(Sequence sequence){

        class RecupererSequenceADelete extends android.os.AsyncTask<Void, Void, Integer>{

            @Override
            protected Integer doInBackground(Void... voids) {

                int nbCycles = mDb
                        .sequenceCycleCrossRefDao()
                        .getNbCycles(sequence.getSequenceId());

                return nbCycles;
            }

            @Override
            protected void onPostExecute(Integer nbCycles){
                super.onPostExecute(nbCycles);

                //si nombre de cycle dans la sequence = 1 on ajoute la sequence à la liste de sequence à supprimer
                if (nbCycles == 1){
                    sequencesAsupprimer.add(sequence);

                    //on va maintenant récupérer tous les entrainements qui contiennent cette sequence sui sera supprimé
                    getEntrainementsAssocies(sequence);
                }
            }
        }

        RecupererSequenceADelete recupererSequenceADelete = new RecupererSequenceADelete();
        recupererSequenceADelete.execute();

    }

    //récupérations de tous les entrainements contenant la sequence qui sera supprimé
    public void getEntrainementsAssocies(Sequence sequenceQuiSeraSupprime){

        class RecupererEntrainementsAssocies extends android.os.AsyncTask<Void, Void, List<Entrainement>>{

            @Override
            protected List<Entrainement> doInBackground(Void... voids) {

                List<Long> entrainementsIds = mDb
                        .entrainementSequenceCrossRefDao()
                        .getEntrainementsId(sequenceQuiSeraSupprime.getSequenceId());

                List<Entrainement> entrainements = mDb
                        .entrainementDao()
                        .getEntrainements(entrainementsIds);

                return entrainements;
            }

            @Override
            protected void onPostExecute(List<Entrainement> entrainements) {
                super.onPostExecute(entrainements);

                //Pour chaque entrainement trouvé
                //on va regarder s'il ne contient qu'une sequence ou non
                for (int i = 0; i < entrainements.size(); i++){
                    checkEntrainement(entrainements.get(i));
                }
            }
        }

        RecupererEntrainementsAssocies recupererEntrainementsAssocies = new RecupererEntrainementsAssocies();
        recupererEntrainementsAssocies.execute();
    }

    //On va regarder sur les entrainements contenant la sequences à supprimer
    //s'ils contiennent d'autres sequences ou non
    //s'ils n'ont pas d'autre sequence, il faudra alors les ajouter à une liste d'entrainement à supprimer
    public void checkEntrainement(Entrainement entrainement){

        class RecupererEntrainementADelete extends android.os.AsyncTask<Void, Void, Integer>{

            @Override
            protected Integer doInBackground(Void... voids) {

                int nbSequences = mDb
                        .entrainementSequenceCrossRefDao()
                        .getNbSequences(entrainement.getEntrainementId());

                return nbSequences;
            }

            @Override
            protected void onPostExecute(Integer nbSequences){
                super.onPostExecute(nbSequences);

                //Si l'entrainement ne contient qu'une sequence (la sequence qui sera supprimé)
                //on ajoute cet entrainement à une liste
                if (nbSequences == 1){
                    entrainementAsupprimer.add(entrainement);
                }
            }
        }

        RecupererEntrainementADelete recupererEntrainementADelete = new RecupererEntrainementADelete();
        recupererEntrainementADelete.execute();

    }

    //methode appelée uniquement si on a au moins un cycle à supprimer en plus du travail
    public void askSuppression(){

        //préparation du message qui va contenir les noms des cycles à supprimer
        String space = " ";
        String message = strSuppressionSupplementaire + "\n";
        message += strCycle + space;

        for (int i = 0; i < cyclesAsupprimer.size(); i++){
            message += cyclesAsupprimer.get(i).getNom() + space;
        }

        //si on a des sequences à supprimer on va les ajouter dans le message
        if (!sequencesAsupprimer.isEmpty()){

            message += "\n";
            message += strSequence + space;
            for (int i = 0 ; i < sequencesAsupprimer.size(); i++){

                message += sequencesAsupprimer.get(i).getNom() + space;
            }
        }

        //si on a des entrainements à supprimer on va les ajouter dans le message
        if (!entrainementAsupprimer.isEmpty()){

            message += "\n";
            message += strEntrainement + space;

            for (int i = 0; i < entrainementAsupprimer.size(); i++){

                message += entrainementAsupprimer.get(i).getNom() + space;
            }
        }


        //on prépare une popup de confirmation de suppression
        // des elements en plus du travail cliqué
        popup2.setTitle(strSuppression);
        popup2.setMessage(message);

        popup2.setPositiveButton(oui, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //si oui on lance la suppression
                suppression();
            }
        });

        popup2.setNegativeButton(non, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //sinon on stop l'activité et on ne supprimera pas le travail non plus
                finish();
            }
        });

        popup2.show();
    }

    //méthode qui va supprimer tout ce qu'on a besoin
    public void suppression(){

        class SupprimerToutAsync extends android.os.AsyncTask<Void, Void, Void> {

            //suppression des objets de la bd
            @Override
            protected Void doInBackground(Void... voids) {
                mDb.travailDao()
                        .delete(travailClicked);

                mDb.cycleDao()
                        .deleteAll(cyclesAsupprimer);

                mDb.sequenceDao()
                        .deleteAll(sequencesAsupprimer);

                mDb.entrainementDao()
                        .deleteAll(entrainementAsupprimer);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
                finish();
            }
        }

        SupprimerToutAsync supprimerTravailAsync = new SupprimerToutAsync();
        supprimerTravailAsync.execute();
    }

    @Override
    protected void onStart(){
        super.onStart();

        //mise à jour des travails
        getTravails();
    }
}