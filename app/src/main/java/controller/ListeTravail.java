package controller;

import androidx.appcompat.app.AppCompatActivity;

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

    //Views
    private ListView listTravail;
    private TextView titrePage;

    //Ressources
    private String liste;
    private String space;
    private String travail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_travail);

        //récupération du boolen si on vient de cycle
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            travails = extras.getParcelableArrayList("arrayListTravails");
            suppression = extras.getBoolean("SUPPRESSION_KEY");
        }

        //On récupère des strings en ressources à concatener
        liste = getResources().getString(R.string.liste);
        space = " ";
        travail = getResources().getString(R.string.travail);
        String strListeTravail = liste+space+travail;

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
                    Travail travailClicked = adapter.getItem(position);

                    class SupprimerTravailAsync extends android.os.AsyncTask<Void, Void, Void> {

                        //suppression de l'objet de la bd
                        @Override
                        protected Void doInBackground(Void... voids) {
                            mDb.travailDao()
                                    .delete(travailClicked);
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

    @Override
    protected void onStart(){
        super.onStart();

        //mise à jour des travails
        getTravails();
    }
}