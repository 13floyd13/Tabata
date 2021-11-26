package controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tabata.R;

import java.util.ArrayList;
import java.util.List;

import data.AppDatabase;
import data.DatabaseClient;
import modele.Entrainement;
import modele.EntrainementAvecSequences;
import modele.EntrainementListAdapter;

public class ListeEntrainement extends AppCompatActivity {

    //Data
    private AppDatabase mDb;
    private EntrainementListAdapter adapter;

    //Attributs
    private ArrayList<EntrainementAvecSequences> entrainements = new ArrayList<EntrainementAvecSequences>();
    private boolean suppression = false;
    private AlertDialog.Builder popup1;
    private ListeEntrainement activity;
    private Entrainement entrainementASupprimer;

    //Views
    private ListView listEntrainement;
    private TextView titrePage;

    //Ressources
    private String liste;
    private String space;
    private String entrainement;
    private String strSuppression;
    private String strsuppressionEntrainement;
    private String oui;
    private String non;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_entrainement);

        this.activity = this;
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            suppression = extras.getBoolean("SUPPRESSION_KEY");
        }

        //on récupère les strings à concaténer
        strSuppression = getResources().getString(R.string.suppression);
        strsuppressionEntrainement = getResources().getString(R.string.suppressionSequence);
        oui = getResources().getString(R.string.oui);
        non = getResources().getString(R.string.non);
        liste = getResources().getString(R.string.liste);
        space = " ";
        entrainement = getResources().getString(R.string.entrainement);
        String strListeEntrainement = liste + space + entrainement;

        //récupération du TextView pour ajouter la string
        titrePage = findViewById(R.id.titrePage);
        titrePage.setText(strListeEntrainement);

        //récupération du ListView
        listEntrainement = findViewById(R.id.listEntrainement);

        // Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();

        //Liaison de l'adapter au listView
        adapter = new EntrainementListAdapter(this, new ArrayList<EntrainementAvecSequences>());
        listEntrainement.setAdapter(adapter);

        //ajout d'un évenement click à la listView
        if (suppression) { //on va supprimer l'entrainement cliqué
            listEntrainement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //préparation de la popup
                    popup1 = new AlertDialog.Builder(activity);

                    EntrainementAvecSequences entrainementAvecSequences = adapter.getItem(position);
                    entrainementASupprimer = entrainementAvecSequences.getEntrainement();

                    //mise en place de la popup de validation de la suppression de la sequence
                    popup1.setTitle(strSuppression);
                    popup1.setMessage(strsuppressionEntrainement + space + entrainementASupprimer.getNom());

                    popup1.setPositiveButton(oui, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //on lance la suppression
                                    suppression();
                                    //fermeture de la popup
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

            //on va jouer l'entrainement cliqué
        } else {

            listEntrainement.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    EntrainementAvecSequences entrainementAvecSequences = adapter.getItem(position);
                    Intent goToPlay = new Intent(getApplicationContext(), Play.class);
                    goToPlay.putParcelableArrayListExtra("sequences_key", entrainementAvecSequences.getSequences());
                    //goToPlay.putExtra("entrainementAvecSequences", entrainementAvecSequences);
                    goToPlay.putExtra("entrainement", entrainementAvecSequences.getEntrainement());
                    startActivity(goToPlay);

                }
            });
        }
    }

        //récupération des entrainements en bd
        private void getEntrainements(){
            class RecupererEntrainementAsync extends android.os.AsyncTask<Void, Void, List<EntrainementAvecSequences>>{

                @Override
                protected List<EntrainementAvecSequences> doInBackground(Void... voids) {
                    List<EntrainementAvecSequences> entrainementList = mDb
                            .entrainementSequenceCrossRefDao()
                            .getEntrainementSequence();
                    return entrainementList;
                }

                @Override
                protected void onPostExecute(List<EntrainementAvecSequences> entrainementsList){
                    super.onPostExecute(entrainementsList);

                    //Mise à jour de l'adapter avec la liste d'entrainements
                    adapter.clear();
                    adapter.addAll(entrainementsList);
                    adapter.notifyDataSetChanged();
                }
            }

            //execution de la demande asynchrone
            RecupererEntrainementAsync recup = new RecupererEntrainementAsync();
            recup.execute();
        }

        //On supprime l'entrainement de la bd
        public void suppression(){

            class SupprimerEntrainementAsync extends android.os.AsyncTask<Void, Void, Void> {

                @Override
                protected Void doInBackground(Void... voids) {
                    mDb.entrainementDao()
                            .delete(entrainementASupprimer);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    adapter.notifyDataSetChanged();
                    finish();
                }
            }

            SupprimerEntrainementAsync supprimerEntrainementAsync = new SupprimerEntrainementAsync();
            supprimerEntrainementAsync.execute();
        }

        @Override
        protected void onStart(){
            super.onStart();

            //mise à jour des entrainements
            getEntrainements();
        }
}