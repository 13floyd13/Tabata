package controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tabata.R;

import java.util.ArrayList;

import data.AppDatabase;
import modele.EntrainementAvecSequences;
import modele.EntrainementListAdapter;

public class ListeEntrainement extends AppCompatActivity {

    //Attributs
    private AppDatabase mDb;
    private EntrainementListAdapter adapter;
    private ListView listEntrainement;
    private ArrayList<EntrainementAvecSequences> entrainements = new ArrayList<EntrainementAvecSequences>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_entrainement);

        //on récupère les strings à concaténer
        String liste = getResources().getString(R.string.liste);
        String space = " ";
        String entrainement = getResources().getString(R.string.entrainement);
        String strListeEntrainement = liste+space+entrainement;

        //récupération du TextView pour ajouter la string
        TextView titrePage = findViewById(R.id.titrePage);
        titrePage.setText(strListeEntrainement);



    }
}