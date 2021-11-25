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
import modele.CycleAvecTravails;
import modele.CycleListAdapter;
import modele.Entrainement;
import modele.Sequence;
import modele.SequenceAvecCycles;
import modele.Travail;
import modele.TravailListAdapter;

public class ListeCycle extends AppCompatActivity {

    //Data
    private AppDatabase mDb;
    private CycleListAdapter adapter;

    //Attributs
    private ArrayList<CycleAvecTravails> cycles = new ArrayList<CycleAvecTravails>();
    private ArrayList<Long> cyclesAjoutes = new ArrayList<>();
    private boolean suppression = false;
    private String nbRepet;
    private String nomSequence;
    private String description;
    private String strTempsReposLong;
    private ArrayList<Sequence> sequencesAsupprimer = new ArrayList<>();
    private ArrayList<Entrainement> entrainementAsupprimer = new ArrayList<>();
    private ListeCycle activity;
    private AlertDialog.Builder popup1;
    private AlertDialog.Builder popup2;
    private Cycle cycleASupprimer;

    //Views
    private ListView listCycle;
    private TextView titrePage;

    //Ressources
    private String liste;
    private String space;
    private String cycle;
    private String strSuppression;
    private String strsuppressionCycle;
    private String strSuppressionSupplementaire;
    private String oui;
    private String non;
    private String strCycle;
    private String strSequence;
    private String strEntrainement;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_cycle);

        this.activity = this;

        //préparation de la popup
        popup1 = new AlertDialog.Builder(activity);
        popup2 = new AlertDialog.Builder(activity);

        //récupération du boolean si on vient de sequence
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            cycles = extras.getParcelableArrayList("arrayListCycles");
            suppression = extras.getBoolean("SUPPRESSION_KEY");
            nbRepet = extras.getString("nbRepet");
            nomSequence = extras.getString("nomSequence");
            description = extras.getString("description");
            strTempsReposLong = extras.getString("strTempsReposLong");
        }

        //on récupère les strings à concaténer
        strSuppression = getResources().getString(R.string.suppression);
        strsuppressionCycle = getResources().getString(R.string.suppressionCycle);
        strSuppressionSupplementaire = getResources().getString(R.string.suppressionSupplementaire);
        strCycle = getResources().getString(R.string.cycle);
        strSequence = getResources().getString(R.string.sequence);
        strEntrainement = getResources().getString(R.string.entrainement);
        oui = getResources().getString(R.string.oui);
        non = getResources().getString(R.string.non);
        liste = getResources().getString(R.string.liste);
        space = " ";
        cycle = getResources().getString(R.string.cycle);
        String strListeCycle = liste+space+cycle;

        //récupération du TextView pour ajouter la string
        titrePage = findViewById(R.id.titrePage);
        titrePage.setText(strListeCycle);

        //récupération du ListView
        listCycle = findViewById(R.id.listCycles);

        // Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();

        //Liaison de l'adapder au listView
        adapter = new CycleListAdapter(this, new ArrayList<CycleAvecTravails>());
        listCycle.setAdapter(adapter);

        if (suppression) { //si on vient du menu Supression

            listCycle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //récupération de l'objet cliqué
                    CycleAvecTravails cycleAvecTravailsClicked = adapter.getItem(position);
                    cycleASupprimer= cycleAvecTravailsClicked.getCycle();

                    //récupération des sequences qui contiennent ce cycle
                    getSequenceAssocies(cycleASupprimer);

                    popup1.setTitle(strSuppression);
                    popup1.setMessage(strsuppressionCycle + space + cycleASupprimer.getNom());

                    popup1.setPositiveButton(oui, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!sequencesAsupprimer.isEmpty()){
                                askSuppression();
                            }else{
                                suppression();
                            }

                            dialog.dismiss();
                        }
                    });

                    popup1.setNegativeButton(non, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            finish();
                        }
                    });

                    popup1.show();

                }
            });

        }else{ //si on vient du menu Creation

            //ajout d'un évenement click à la listeView
            listCycle.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //Récupération du travail cliqué pour l'envoyer à la création du cycle dans un arayList de travail
                    CycleAvecTravails cycleClicked = adapter.getItem(position);

                    //boucle pour récuperer les id des cycles ajoutés à la creation de sequence
                    for(int i = 0; i < cycles.size(); i++){
                        cyclesAjoutes.add(cycles.get(i).getCycle().getCycleId());
                    }

                    //on vérifie que le cycle n'a pas déja été ajouté
                    if(cyclesAjoutes.contains(cycleClicked.getCycle().getCycleId())){
                        Toast toast = Toast.makeText(ListeCycle.this, "Cycle déja ajouté", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER, 20, 30);
                        toast.show();
                    }else { //si pas ajouté on l'ajoute à la creation de sequence
                        Intent goBacktoSequence = new Intent(getApplicationContext(), CreationSequence.class);
                        cycles.add(cycleClicked);
                        goBacktoSequence.putParcelableArrayListExtra("arrayListCycleClicked", cycles);
                        goBacktoSequence.putExtra("nomSequence", nomSequence);
                        goBacktoSequence.putExtra("nbRepet", nbRepet);
                        goBacktoSequence.putExtra("strTempsReposLong", strTempsReposLong);
                        goBacktoSequence.putExtra("description", description);
                        goBacktoSequence.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(goBacktoSequence);
                    }
                }
            });
        }
    }

    //récupération des cycles en bd
    private void getCycles(){

        class RecupererCycleAsync extends android.os.AsyncTask<Void, Void, List<CycleAvecTravails>>{

            @Override
            protected List<CycleAvecTravails> doInBackground(Void... voids) {
                List<CycleAvecTravails> cycleList = mDb
                        .cycleDao()
                        .getAll();
                return cycleList;
            }

            @Override
            protected void onPostExecute(List<CycleAvecTravails> cycles){
                super.onPostExecute(cycles);

                //Mise à jour de l'adapter avec la liste des cycles
                adapter.clear();
                adapter.addAll(cycles);
                adapter.notifyDataSetChanged();
            }
        }

        //execution de la demande asynchrone
        RecupererCycleAsync recup = new RecupererCycleAsync();
        recup.execute();
    }


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
                    checkSequence(sequences.get(i));
                }
            }

        }

        RecupererSequenceAssocies recupererSequenceAssocies = new RecupererSequenceAssocies();
        recupererSequenceAssocies.execute();

    }

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

                if (nbCycles == 1){
                    sequencesAsupprimer.add(sequence);
                    getEntrainementsAssocies(sequence);
                }
            }
        }

        RecupererSequenceADelete recupererSequenceADelete = new RecupererSequenceADelete();
        recupererSequenceADelete.execute();

    }

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

                for (int i = 0; i < entrainements.size(); i++){
                    checkEntrainement(entrainements.get(i));
                }
            }
        }

        RecupererEntrainementsAssocies recupererEntrainementsAssocies = new RecupererEntrainementsAssocies();
        recupererEntrainementsAssocies.execute();
    }

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

                if (nbSequences == 1){
                    entrainementAsupprimer.add(entrainement);
                }
            }
        }

        RecupererEntrainementADelete recupererEntrainementADelete = new RecupererEntrainementADelete();
        recupererEntrainementADelete.execute();

    }

    public void askSuppression(){

        String space = " ";
        String message = strSuppressionSupplementaire + "\n";
        message += strSequence + space;

        for (int i = 0 ; i < sequencesAsupprimer.size(); i++){

            message += sequencesAsupprimer.get(i).getNom() + space;

        }

        if (!entrainementAsupprimer.isEmpty()){

            message += "\n";
            message += strEntrainement + space;

            for (int i = 0; i < entrainementAsupprimer.size(); i++){

                message += entrainementAsupprimer.get(i).getNom() + space;
            }
        }


        popup2.setTitle(strSuppression);
        popup2.setMessage(message);

        popup2.setPositiveButton(oui, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                suppression();
            }
        });

        popup2.setNegativeButton(non, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        popup2.show();
    }

    public void suppression(){

        class SupprimerToutAsync extends android.os.AsyncTask<Void, Void, Void> {

            //suppression de l'objet de la bd
            @Override
            protected Void doInBackground(Void... voids) {

                mDb.cycleDao()
                        .delete(cycleASupprimer);

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

        //mise à jour des cycles
        getCycles();
    }
}