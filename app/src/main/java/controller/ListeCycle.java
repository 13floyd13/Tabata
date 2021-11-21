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
import modele.CycleListAdapter;
import modele.Sequence;
import modele.SequenceAvecCycles;
import modele.Travail;
import modele.TravailListAdapter;

public class ListeCycle extends AppCompatActivity {

    private AppDatabase mDb;
    private CycleListAdapter adapter;
    private ListView listCycle;
    private ArrayList<CycleAvecTravails> cycles = new ArrayList<CycleAvecTravails>();
    private boolean suppression = false;
    private ArrayList<Long> cyclesAjoutes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_cycle);

        //récupération du boolean si on vient de sequence
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            cycles = extras.getParcelableArrayList("arrayListCycles");
            suppression = extras.getBoolean("SUPPRESSION_KEY");
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

        if (suppression) { //si on vient du menu Supression
            listCycle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    CycleAvecTravails cycleAvecTravailsClicked = adapter.getItem(position);
                    Cycle cycle= cycleAvecTravailsClicked.getCycle();


                    class SupprimerCycleAsync extends android.os.AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            mDb.cycleDao()
                                    .delete(cycle);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            adapter.notifyDataSetChanged();

                        }
                    }

                    SupprimerCycleAsync supprimerCycleAsync = new SupprimerCycleAsync();
                    supprimerCycleAsync.execute();
                    finish();
                }
            });
        }else{ //si on vient du menu Creation
            //ajout d'un évenement click à la listeView
            listCycle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //Récupération du travail cliqué pour l'envoyer à la création du cycle dans un arayList de travail
                    CycleAvecTravails cycleClicked = adapter.getItem(position);

                    //boucle pour récuperer les id des cycles ajoutés à la creation de sequence
                    for(int i = 0; i < cycles.size(); i++){
                        cyclesAjoutes.add(cycles.get(i).getCycle().getCycleId());
                    }

                    //on vérifie que le cycle n'a pas déja été ajouté
                    if(cyclesAjoutes.contains(cycleClicked.getCycle().getCycleId())){
                        Toast toast = Toast.makeText(ListeCycle.this, "Cycle déja ajouté", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER, 20, 30);
                        toast.show();
                    }else { //si pas ajouté on l'ajoute à la creation de sequence
                        Intent goBacktoSequence = new Intent(getApplicationContext(), CreationSequence.class);
                        cycles.add(cycleClicked);
                        goBacktoSequence.putParcelableArrayListExtra("arrayListCycleClicked", cycles);
                        goBacktoSequence.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(goBacktoSequence);
                    }
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