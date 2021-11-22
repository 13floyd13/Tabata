package controller;

import androidx.appcompat.app.AppCompatActivity;

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

    //Views
    private ListView listEntrainement;
    private TextView titrePage;

    //Ressources
    private String liste;
    private String space;
    private String entrainement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_entrainement);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            suppression = extras.getBoolean("SUPPRESSION_KEY");
        }

        //on récupère les strings à concaténer
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

                    EntrainementAvecSequences entrainementAvecSequences = adapter.getItem(position);
                    Entrainement entrainement = entrainementAvecSequences.getEntrainement();


                    class SupprimerEntrainementAsync extends android.os.AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            mDb.entrainementDao()
                                    .delete(entrainement);
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
            });

        } else {  //on va jouer l'entrainement cliqué

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

        @Override
        protected void onStart(){
            super.onStart();

            //mise à jour des entrainements
            getEntrainements();
        }
}