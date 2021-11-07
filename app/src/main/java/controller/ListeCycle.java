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
import modele.Cycle;
import modele.CycleAvecTravails;
import modele.CycleListAdapter;
import modele.Travail;
import modele.TravailListAdapter;

public class ListeCycle extends AppCompatActivity {

    private AppDatabase mDb;
    private CycleListAdapter adapter;
    private ListView listCycle;
    private boolean sequence = false;
    private ArrayList<CycleAvecTravails> cycles = new ArrayList<CycleAvecTravails>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_cycle);

        //récupération du boolean si on vient de sequence
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            sequence = extras.getBoolean("SEQUENCE_KEY");
            cycles = extras.getParcelableArrayList("arrayListCycles");
        }

        //on récupère les strings à concaténer
        String liste = getResources().getString(R.string.liste);
        String space = " ";
        String cycle = getResources().getString(R.string.cycle);
        String strListeCycle = liste+space+cycle;

        //récupération du TextView pour ajouter la string
        TextView titrePage = findViewById(R.id.titrePage);
        titrePage.setText(strListeCycle);

        //récupération du ListView
        listCycle = findViewById(R.id.listCycles);

        // Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();

        //Liaison de l'adapder au listView
        adapter = new CycleListAdapter(this, new ArrayList<CycleAvecTravails>());
        listCycle.setAdapter(adapter);

        if(sequence) { //on ajoute un evenement click uniquement si on vient d'une sequence

            //ajout d'un évenement click à la listeView
            listCycle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //Récupération du travail cliqué pour l'envoyer à la création du cycle dans un arayList de travail
                    CycleAvecTravails cycleClicked = adapter.getItem(position);
                    Intent goBacktoSequence = new Intent(getApplicationContext(), CreationSequence.class);
                    cycles.add(cycleClicked);
                    goBacktoSequence.putParcelableArrayListExtra("arrayListCycleClicked", cycles);
                    goBacktoSequence.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(goBacktoSequence);
                }
            });
        }
    }

    private void getCycles(){

        class RecupererCycleAsync extends android.os.AsyncTask<Void, Void, List<CycleAvecTravails>>{

            @Override
            protected List<CycleAvecTravails> doInBackground(Void... voids) {
                List<CycleAvecTravails> cycleList = mDb
                        .cycleDao()
                        .getAll();
                return cycleList;
            }

            @Override
            protected void onPostExecute(List<CycleAvecTravails> cycles){
                super.onPostExecute(cycles);

                //Mise à jour de l'adapter avec la liste des cycles
                adapter.clear();
                adapter.addAll(cycles);
                adapter.notifyDataSetChanged();
            }
        }

        //execution de la demande asynchrone
        RecupererCycleAsync recup = new RecupererCycleAsync();
        recup.execute();
    }

    @Override
    protected void onStart(){
        super.onStart();

        //mise à jour des cycles
        getCycles();
    }
}