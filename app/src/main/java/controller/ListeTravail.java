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
import modele.Travail;
import modele.TravailListAdapter;

public class ListeTravail extends AppCompatActivity {

    private AppDatabase mDb;
    private TravailListAdapter adapter;
    private ListView listTravail;
    private boolean cycle=false;
    private ArrayList<Travail> travails = new ArrayList<Travail>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_travail);

        //récupération du boolen si on vient de cycle
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            cycle = extras.getBoolean("CYCLE_KEY");
            travails = extras.getParcelableArrayList("arrayListTravails");
        }

        //On récupère des strings en ressources à concatener
        String liste = getResources().getString(R.string.liste);
        String space = " ";
        String travail = getResources().getString(R.string.travail);
        String strListeTravail = liste+space+travail;

        //récupération du TextView de temps de travail pour ajouter la string
        TextView titrePage = findViewById(R.id.titrePage);
        titrePage.setText(strListeTravail);

        //récupération du ListView
        listTravail = findViewById(R.id.listTravails);

        // Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();

        //Liaison de l'adapder au listView
        adapter = new TravailListAdapter(this, new ArrayList<Travail>());
        listTravail.setAdapter(adapter);

        if(cycle) { //on ajoute un evenement click uniquement si on vient d'un cycle

            //ajout d'un évenement click à la listeView
            listTravail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //Récupération du travail cliqué pour l'envoyer à la création du cycle dans un arayList de travail
                    Travail travailClicked = adapter.getItem(position);
                    Intent goBacktoCycle = new Intent(getApplicationContext(), CreationCycle.class);
                    travails.add(travailClicked);
                    goBacktoCycle.putParcelableArrayListExtra("arrayListTravailsClicked", travails);
                    goBacktoCycle.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(goBacktoCycle);
                }
            });
        }
    }

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