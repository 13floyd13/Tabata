package controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_travail);

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