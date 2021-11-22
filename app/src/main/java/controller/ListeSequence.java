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
import modele.CycleAvecTravails;
import modele.CycleListAdapter;
import modele.Entrainement;
import modele.EntrainementAvecSequences;
import modele.Sequence;
import modele.SequenceAvecCycles;
import modele.SequenceListAdapter;

public class ListeSequence extends AppCompatActivity {

    //Data
    private AppDatabase mDb;
    private SequenceListAdapter adapter;

    //Attributs
    private ArrayList<SequenceAvecCycles> sequences = new ArrayList<SequenceAvecCycles>();
    private boolean suppression = false;
    private ArrayList<Long> sequencesAjoutes = new ArrayList<>();
    private String nomEntrainement;

    //Views
    private ListView listSequence;
    private TextView titrePage;

    //ressources
    private String liste;
    private String space;
    private String sequence;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_sequence);

        //récupération du boolean si on vient d'entrainement
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sequences = extras.getParcelableArrayList("arrayListSequences");
            suppression = extras.getBoolean("SUPPRESSION_KEY");
            nomEntrainement = extras.getString("nomEntrainement");
        }

        //on récupère les strings à concaténer
        liste = getResources().getString(R.string.liste);
        space = " ";
        sequence = getResources().getString(R.string.sequence);
        String strListeSequence = liste + space + sequence;

        //récupération du TextView pour ajouter la string
        titrePage = findViewById(R.id.titrePage);
        titrePage.setText(strListeSequence);

        //récupération du ListView
        listSequence = findViewById(R.id.listSequence);

        // Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();

        //Liaison de l'adapder au listView
        adapter = new SequenceListAdapter(this, new ArrayList<SequenceAvecCycles>());
        listSequence.setAdapter(adapter);

        if (suppression) {
            listSequence.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //récupération de l'objet cliqué
                    SequenceAvecCycles sequenceAvecCyclesClicked = adapter.getItem(position);
                    Sequence sequence = sequenceAvecCyclesClicked.getSequence();

                    class SupprimerSequenceAsync extends android.os.AsyncTask<Void, Void, Void> {

                        //suppression de la sequence de la bd
                        @Override
                        protected Void doInBackground(Void... voids) {
                            mDb.sequenceDao()
                                    .delete(sequence);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            adapter.notifyDataSetChanged();
                            finish();
                        }
                    }

                    SupprimerSequenceAsync supprimerSequenceAsync = new SupprimerSequenceAsync();
                    supprimerSequenceAsync.execute();

                }
            });

        }else {

            //ajout d'un évenement click à la listeView
            listSequence.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //Récupération de la séquence cliqué
                    SequenceAvecCycles sequenceClicked = adapter.getItem(position);

                    //boucle pour récuperer les id des sequences déja ajoutés à la creation d'entrainement
                    for (int i = 0; i < sequences.size(); i++){
                        sequencesAjoutes.add(sequences.get(i).getSequence().getSequenceId());
                    }

                    //on vérifie que la sequence n'a pas déja été cliqué
                    if (sequencesAjoutes.contains(sequenceClicked.getSequence().getSequenceId())) {

                        Toast toast = Toast.makeText(ListeSequence.this, "Sequence déja ajouté", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER, 20, 30);
                        toast.show();

                    }else {
                        Intent goBacktoEntrainement = new Intent(getApplicationContext(), CreationEntrainement.class);
                        sequences.add(sequenceClicked);
                        goBacktoEntrainement.putParcelableArrayListExtra("arrayListSequenceClicked", sequences);
                        goBacktoEntrainement.putExtra("nomEntrainement", nomEntrainement);
                        goBacktoEntrainement.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(goBacktoEntrainement);
                    }
                }
            });
        }
    }

    //récupération des sequences en bd
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