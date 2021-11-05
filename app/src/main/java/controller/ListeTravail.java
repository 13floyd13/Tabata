package controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.tabata.R;

import java.util.List;

import data.AppDatabase;
import data.DatabaseClient;
import modele.Travail;

public class ListeTravail extends AppCompatActivity {

    private AppDatabase mDb;
    List<Travail> travails;


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


        // Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();

        //execution de la récupération des travails en base de donnée
        new RecupererTravailAsync().execute();


    }

    public class RecupererTravailAsync extends android.os.AsyncTask<Void, Void, List<Travail>>{

        @Override
        protected List<Travail> doInBackground(Void... voids) {
            travails = mDb.travailDao().getAll();
            return travails;
        }
    }
}