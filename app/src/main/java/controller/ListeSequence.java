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


        if(entrainement) { //on ajoute un evenement click uniquement si on vient d'un entrainement

            //ajout d'un évenement click à la listeView
            listSequence.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //Récupération du travail cliqué pour l'envoyer à la création du cycle dans un arayList de travail
                    SequenceAvecCycles sequenceClicked = adapter.getItem(position);
                    Intent goBacktoEntrainement = new Intent(getApplicationContext(), CreationEntrainement.class);
                    sequences.add(sequenceClicked);
                    goBacktoEntrainement.putParcelableArrayListExtra("arrayListSequenceClicked", sequences);
                    goBacktoEntrainement.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(goBacktoEntrainement);
                }
            });
        }
    }
    private void getSequences(){

        class RecupererSequenceAsync extends android.os.AsyncTask<Void, Void, List<SequenceAvecCycles>>{

            @Override
            protected List<SequenceAvecCycles> doInBackground(Void... voids) {
                List<SequenceAvecCycles> sequenceList = mDb
                        .sequenceDao()
                        .getAll();
                return sequenceList;
            }

            @Override
            protected void onPostExecute(List<SequenceAvecCycles> sequencesList){
                super.onPostExecute(sequencesList);

                //Mise à jour de l'adapter avec la liste de sequences
                adapter.clear();
                adapter.addAll(sequencesList);
                adapter.notifyDataSetChanged();
            }
        }

        //execution de la demande asynchrone
        RecupererSequenceAsync recup = new RecupererSequenceAsync();
        recup.execute();
    }

    @Override
    protected void onStart(){
        super.onStart();

        //mise à jour des sequences
        getSequences();
    }
}