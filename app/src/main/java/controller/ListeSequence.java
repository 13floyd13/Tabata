package controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tabata.R;

import java.util.ArrayList;

import data.AppDatabase;
import data.DatabaseClient;
import modele.CycleAvecTravails;
import modele.CycleListAdapter;
import modele.SequenceAvecCycles;
import modele.SequenceListAdapter;

public class ListeSequence extends AppCompatActivity {


    private AppDatabase mDb;
    private SequenceListAdapter adapter;
    private ListView listSequence;
    private boolean entrainement = false;
    private ArrayList<SequenceAvecCycles> sequences = new ArrayList<SequenceAvecCycles>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_sequence);

        //récupération du boolean si on vient d'entrainement
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            entrainement = extras.getBoolean("ENTRAINEMENT_KEY");
            sequences = extras.getParcelableArrayList("arrayListSequence");
        }

        //on récupère les strings à concaténer
        String liste = getResources().getString(R.string.liste);
        String space = " ";
        String sequence = getResources().getString(R.string.sequence);
        String strListeSequence = liste+space+sequence;

        //récupération du TextView pour ajouter la string
        TextView titrePage = findViewById(R.id.titrePage);
        titrePage.setText(strListeSequence);

        //récupération du ListView
        listSequence = findViewById(R.id.listSequence);

        // Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();

        //Liaison de l'adapder au listView
        adapter = new SequenceListAdapter(this, new ArrayList<SequenceAvecCycles>());
        listSequence.setAdapter(adapter);



    }
}