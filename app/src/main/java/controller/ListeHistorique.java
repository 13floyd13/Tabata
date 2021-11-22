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
import modele.Historique;
import modele.HistoriqueListAdapter;
import modele.Travail;
import modele.TravailListAdapter;

public class ListeHistorique extends AppCompatActivity {

    //Data
    private AppDatabase mDb;
    private HistoriqueListAdapter adapter;

    //Attributs
    private ArrayList<Historique> historiques = new ArrayList<Historique>();

    //Views
    private ListView listHistorique;
    private TextView titrePage;

    //Ressources
    private String historique;
    private String space;
    private String entrainement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_historique);

        //On récupère des strings en ressources à concatener
        historique = getResources().getString(R.string.historique);
        space = " ";
        entrainement = getResources().getString(R.string.entrainement);

        String strHistoriqueEntrainement = historique + space + entrainement;

        //récupération du TextView de temps de travail pour ajouter la string
        titrePage = findViewById(R.id.titrePage);
        titrePage.setText(strHistoriqueEntrainement);

        //récupération du ListView
        listHistorique = findViewById(R.id.listHistorique);

        // Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();

        //Liaison de l'adapder au listView
        adapter = new HistoriqueListAdapter(this, new ArrayList<Historique>());
        listHistorique.setAdapter(adapter);
    }

    //récupération des historiques en bd
    private void getHistorique() {

        class RecupHistoriqueAsync extends android.os.AsyncTask<Void, Void, List<Historique>> {

            @Override
            protected List<Historique> doInBackground(Void... voids) {

                List<Historique> historiqueList = mDb
                        .historiqueDao()
                        .getAll();

                return historiqueList;
            }

            @Override
            protected void onPostExecute(List<Historique> historiques) {
                super.onPostExecute(historiques);

                //Mise à jour de l'adapter
                adapter.clear();
                adapter.addAll(historiques);
                adapter.notifyDataSetChanged();
            }
        }

        RecupHistoriqueAsync recupHistoriqueAsync = new RecupHistoriqueAsync();
        recupHistoriqueAsync.execute();
    }

    @Override
    protected void onStart(){
        super.onStart();

        //mise à jour des travails
        getHistorique();
    }
}