
package controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tabata.R;

import java.util.ArrayList;
import java.util.List;

import data.DatabaseClient;
import modele.Cycle;
import modele.CycleAvecTravails;
import modele.CycleListAdapter;
import modele.Sequence;

public class CreationSequence extends AppCompatActivity {

    private static final boolean SEQUENCE_KEY = true;
    private String nomSequence;
    private DatabaseClient mDb;
    private ArrayList<CycleAvecTravails> cyclesAvecTravails = new ArrayList<CycleAvecTravails>();
    int tempsReposLong;
    private String description;
    private ListView listCycleClicked;
    private CycleListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_sequence);

        //récupération des cycles ajoutés
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cyclesAvecTravails = extras.getParcelableArrayList("arrayListCycleClicked");
        }

        //Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext());

        //On récupère des strings en ressources à concatener
        String ajouter = getResources().getString(R.string.ajouter);
        String space = " ";
        String create = getResources().getString(R.string.creer);
        String cycle = getResources().getString(R.string.cycle);

        String strCreateCycle = create+space+cycle;
        String strAjouterCycle = ajouter+space+cycle;

        //récupértion du bouton d'ajout de cycle pour ajouter la string
        Button buttonAddCycle = findViewById(R.id.buttonAjouterCycle);
        buttonAddCycle.setText(strAjouterCycle);

        //récupération du bouton de création de cycle pour ajouter la string
        Button buttonCreateCycle = findViewById(R.id.buttonCreerCycle);
        buttonCreateCycle.setText(strCreateCycle);

        //récupération de la listView de cycles ajoutés
        listCycleClicked = findViewById(R.id.listCycles);

        if (cyclesAvecTravails != null && !cyclesAvecTravails.isEmpty()){

            //Liaison de l'adapter au listView
            adapter = new CycleListAdapter(this, new ArrayList<CycleAvecTravails>());
            adapter.clear();
            adapter.addAll(cyclesAvecTravails);
            listCycleClicked.setAdapter(adapter);
        }
    }

    public void onAjouterCycle(View view) {
        Intent goToListCycle = new Intent(getApplicationContext(), ListeCycle.class);
        goToListCycle.putExtra("SEQUENCE_KEY", SEQUENCE_KEY);
        goToListCycle.putParcelableArrayListExtra("arrayListCycles", cyclesAvecTravails);
        startActivity(goToListCycle);
    }

    public void onCreerCycle(View view) {
        Intent goToCreateCycle = new Intent(getApplicationContext(), CreationCycle.class);
        startActivity(goToCreateCycle);
    }

    public void onSave(View view) {

        //récupértion du nom de la séquence
        EditText eNomSequence = findViewById(R.id.nomSequence);
        nomSequence = eNomSequence.getText().toString();

        //on vérifie que le nom de la séquence ne soit pas vide
        if(nomSequence.isEmpty()){
            Toast toast = Toast.makeText(CreationSequence.this, "Nom de séquence obligatoire", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 20, 30);
            toast.show();
            return;
        }

        //récupération de la liste des cycles ajoutées
        ListView listViewCycle = findViewById(R.id.listCycles);

        //on vérifie qu'au moins un cycle soit ajoutée
        if (listViewCycle.getCount() == 0){
            Toast toast = Toast.makeText(CreationSequence.this, "Vous devez ajouter au moins un cycle", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 20, 30);
            toast.show();
            return;
        }

        //remplissage de la List de Cycle
        /*for (int i = 0; i < listViewCycle.getCount(); i++){

        Cycle cycle = (Cycle) listViewCycle.getItemAtPosition(i);
        listeCycle.add(cycle);
        }*/

        //récupération du temps de repos long
        EditText eTempsReposLong = findViewById(R.id.tempsRepos);
        String strTempsReposLong = eTempsReposLong.getText().toString();

        //on vérifie si le temps de repos est rempli sinon on le met à 60 secondes par défaut
        if(strTempsReposLong.isEmpty()){
            tempsReposLong = 60;
        }else{
            tempsReposLong = Integer.parseInt(strTempsReposLong);
        }

        //récupération de la description de la séquence
        EditText eDescription = findViewById(R.id.descriptionSequence);
        String description = eDescription.getText().toString();


        //Création classe asynchrone pour sauvegarder la séquence
        class SaveSequence extends AsyncTask<Void, Void, Sequence>{

            @Override
            protected Sequence doInBackground(Void... voids) {

                //Création de l'objet Sequence
                Sequence sequence = new Sequence(nomSequence);

                //Ajout de la description et du temps de Repos Long
                if(strTempsReposLong.isEmpty()){
                    sequence.setTempsReposLong(60);
                }else{
                    sequence.setTempsReposLong(Integer.parseInt(strTempsReposLong));
                    //tempsReposLong = Integer.parseInt(strTempsReposLong);
                }
                sequence.setDescription(description);
                //sequence.setTempsReposLong(tempsReposLong);

                // enregistrement de sequence en bdd
                mDb.getAppDatabase()
                        .sequenceDao()
                        .insert(sequence);

                //enregistrement des cycles dans la table Sequence
                ArrayList cycles = new ArrayList<Cycle>();
                for (int i = 0; i < cyclesAvecTravails.size(); i++){
                    cycles.add(cyclesAvecTravails.get(i).getCycle());
                }
                mDb.getAppDatabase()
                        .sequenceDao()
                        .insertCycles(cycles);

                return sequence;
            }
        }

        SaveSequence saveSequence = new SaveSequence();
        saveSequence.execute();

    }
}