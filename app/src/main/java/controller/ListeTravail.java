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
import modele.CycleTravailCrossRef;
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
    private ArrayList<Cycle> cycles = new ArrayList<>();
    private ArrayList<Long> cyclesIds = new ArrayList<>();
    private ListeTravail activity;
    private AlertDialog.Builder popup;
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
    private String strSuppressionSupplementaireCycle;
    private String oui;
    private String non;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_travail);

        this.activity = this;

        //préparation de la popup
        popup = new AlertDialog.Builder(activity);

        //récupération du boolen si on vient de cycle
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
        strSuppressionSupplementaireCycle = getResources().getString(R.string.suppressionSupplementaireCycle);
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


        if (suppression) {

            listTravail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //récupération du travail cliqué
                    travailClicked = adapter.getItem(position);

                    //récupération des cycles qui contiennent ce travail
                    getCyclesAssociated();


                }
            });

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

    //récupération des  id des cycles qui contiennent le travail cliqué
    public void getCyclesAssociated(){

        class RecupererCyclesAssociesAsync extends android.os.AsyncTask<Void, Void, List<Long>>{

            @Override
            protected List<Long> doInBackground(Void... voids) {

                List<Long> idsCycle = mDb
                        .cycleTravailCrossRefDao()
                        .getCycleIds(travailClicked.getTravailId());


                return idsCycle;
            }

            @Override
            protected void onPostExecute(List<Long> idsCycle){
                super.onPostExecute(idsCycle);

                for (int i = 0; i < idsCycle.size(); i++){


                    cyclesIds.addAll(idsCycle);

                    //récupération des cycles qui devront être supprimés
                    getCyclesToDelete();

                }

            }

        }

        RecupererCyclesAssociesAsync recupererCyclesAssociesAsync = new RecupererCyclesAssociesAsync();
        recupererCyclesAssociesAsync.execute();

    }

    //récupération des cycles ne contenant que le travail cliqué
    //s'ils contiennent que le travail cliqué on doit les supprimer pour ne pas avoir de cycle vide dans l'application
    public void getCyclesToDelete(){

        class RecupererCyclesADelete extends android.os.AsyncTask<Void, Void, ArrayList<CycleAvecTravails>>{

            @Override
            protected ArrayList<CycleAvecTravails> doInBackground(Void... voids) {

                ArrayList<CycleAvecTravails> cycles1 = mDb
                        .cycleDao()
                        .getCyclesAvecTravails(cyclesIds);
                return cycles1;
            }

            @Override
            protected void onPostExecute(ArrayList<CycleAvecTravails> cycleAvecTravails){
                super.onPostExecute(cycleAvecTravails);


                for (int i = 0; i < cycleAvecTravails.size(); i++){
                    List<Travail> travails = cycleAvecTravails.get(i).getTravails();

                    if (travails.size() == 1){
                        cycles.add(cycleAvecTravails.get(i).getCycle());
                    }
                }
                askSuppression();
            }
        }

        RecupererCyclesADelete recupererCyclesADelete = new RecupererCyclesADelete();
        recupererCyclesADelete.execute();

    }

    public void askSuppression(){

        String space = " ";
        String message = strsuppressionTravail + space + travailClicked.getNom() + "\n";
        if(!cycles.isEmpty()){
            for (int i = 0; i < cycles.size(); i++){
                message += cycles.get(i).getNom() + space;
            }
        }
        popup.setTitle(strSuppression);
        popup.setMessage(message);

        popup.setPositiveButton(oui, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                suppression();
            }
        });

        popup.setNegativeButton(non, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        popup.show();
    }

    public void suppression(){

        class SupprimerTravailAsync extends android.os.AsyncTask<Void, Void, Void> {

            //suppression de l'objet de la bd
            @Override
            protected Void doInBackground(Void... voids) {
                mDb.travailDao()
                        .delete(travailClicked);

                for (int i = 0; i < cycles.size(); i++){
                    mDb.cycleDao()
                            .delete(cycles.get(i));
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
                finish();
            }
        }

        SupprimerTravailAsync supprimerTravailAsync = new SupprimerTravailAsync();
        supprimerTravailAsync.execute();
    }

    @Override
    protected void onStart(){
        super.onStart();

        //mise à jour des travails
        getTravails();
    }
}