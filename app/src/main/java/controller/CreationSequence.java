
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
import modele.SequenceCycleCrossRef;
import modele.Travail;

public class CreationSequence extends AppCompatActivity {

    //Data
    private DatabaseClient mDb;
    private CycleListAdapter adapter;

    //Attributs
    private int tempsReposLong;
    private ArrayList<CycleAvecTravails> cyclesAvecTravails = new ArrayList<CycleAvecTravails>();
    private int nbRepetitions;
    private String nbRepet;
    private String nomSequence;
    private String description;
    private String strTempsReposLong;
    private int repet;


    //Views
    private ListView listCycleClicked;
    private Button buttonAddCycle;
    private Button buttonCreateCycle;
    private EditText eNomSequence;
    private EditText eNbRepetitions;
    private ListView listViewCycle;
    private EditText eTempsReposLong;
    private EditText eDescription;

    //Ressources
    private String ajouter;
    private String space;
    private String create;
    private String cycle;
    private String sequenceObligatoire;
    private String miniCycle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_sequence);

        //récupération des cycles ajoutés
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cyclesAvecTravails = extras.getParcelableArrayList("arrayListCycleClicked");
            nbRepet = extras.getString("nbRepet");
            nomSequence = extras.getString("nomSequence");
            description = extras.getString("description");
            strTempsReposLong = extras.getString("strTempsReposLong");
            repet = extras.getInt("repet");
        }

        //on set le nb de repetitions pour que ça apparaisse dans la liste
        if(!cyclesAvecTravails.isEmpty()){

            cyclesAvecTravails.get(cyclesAvecTravails.size()-1).setNbRepet(repet);
        }

        //On test si on a reçu quelque chose via le getIntent et si oui on l'introduit dans les editText liés
        if (nbRepet != null){
            eNbRepetitions = findViewById(R.id.nbRepetitions);
            eNbRepetitions.setText(nbRepet);
        }

        if (nomSequence != null){
            eNomSequence = findViewById(R.id.nomSequence);
            eNomSequence.setText(nomSequence);
        }

        if (description != null){
            eDescription = findViewById(R.id.descriptionSequence);
            eDescription.setText(description);
        }

        if (strTempsReposLong != null){
            eTempsReposLong = findViewById(R.id.tempsRepos);
            eTempsReposLong.setText(strTempsReposLong);
        }

        //Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext());

        //On récupère des strings en ressources à concatener
        ajouter = getResources().getString(R.string.ajouter);
        space = " ";
        create = getResources().getString(R.string.creer);
        cycle = getResources().getString(R.string.cycle);
        sequenceObligatoire = getResources().getString(R.string.sequenceObligatoire);
        miniCycle = getResources().getString(R.string.miniCycle);

        String strCreateCycle = create+space+cycle;
        String strAjouterCycle = ajouter+space+cycle;

        //récupértion du bouton d'ajout de cycle pour ajouter la string
        buttonAddCycle = findViewById(R.id.buttonAjouterCycle);
        buttonAddCycle.setText(strAjouterCycle);

        //récupération du bouton de création de cycle pour ajouter la string
        buttonCreateCycle = findViewById(R.id.buttonCreerCycle);
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
        //récupération des editText pour ne pas perdre les infos entre les activités
        eNomSequence = findViewById(R.id.nomSequence);
        nomSequence = eNomSequence.getText().toString();

        eNbRepetitions = findViewById(R.id.nbRepetitions);
        nbRepet = eNbRepetitions.getText().toString();

        eTempsReposLong = findViewById(R.id.tempsRepos);
        strTempsReposLong = eTempsReposLong.getText().toString();

        eDescription = findViewById(R.id.descriptionSequence);
        description = eDescription.getText().toString();

        Intent goToListCycle = new Intent(getApplicationContext(), ListeCycle.class);

        //envoie des cycles déja ajoutés au préalable dans la séquence
        goToListCycle.putParcelableArrayListExtra("arrayListCycles", cyclesAvecTravails);
        goToListCycle.putExtra("nomSequence", nomSequence);
        goToListCycle.putExtra("nbRepet", nbRepet);
        goToListCycle.putExtra("strTempsReposLong", tempsReposLong);
        goToListCycle.putExtra("description", description);
        startActivity(goToListCycle);
    }

    public void onCreerCycle(View view) {
        Intent goToCreateCycle = new Intent(getApplicationContext(), CreationCycle.class);
        startActivity(goToCreateCycle);
    }

    public void onSave(View view) {

        //récupértion du nom de la séquence
        eNomSequence = findViewById(R.id.nomSequence);
        nomSequence = eNomSequence.getText().toString();

        //on vérifie que le nom de la séquence ne soit pas vide
        if(nomSequence.isEmpty()){
            Toast toast = Toast.makeText(CreationSequence.this, sequenceObligatoire, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 20, 30);
            toast.show();
            return;
        }

        eNbRepetitions = findViewById(R.id.nbRepetitions);
        nbRepet = eNbRepetitions.getText().toString();
        if (nbRepet.isEmpty()){
            nbRepetitions = 4;
        }else {
            nbRepetitions = Integer.parseInt(nbRepet);
        }

        //récupération de la liste des cycles ajoutées
        listViewCycle = findViewById(R.id.listCycles);

        //on vérifie qu'au moins un cycle soit ajoutée
        if (listViewCycle.getCount() == 0){
            Toast toast = Toast.makeText(CreationSequence.this, miniCycle, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 20, 30);
            toast.show();
            return;
        }


        //récupération du temps de repos long
        eTempsReposLong = findViewById(R.id.tempsRepos);
        strTempsReposLong = eTempsReposLong.getText().toString();


        //récupération de la description de la séquence
        eDescription = findViewById(R.id.descriptionSequence);
        String description = eDescription.getText().toString();


        //Création classe asynchrone pour sauvegarder la séquence
        class SaveSequence extends AsyncTask<Void, Void, Sequence>{

            @Override
            protected Sequence doInBackground(Void... voids) {

                //Création de l'objet Sequence
                Sequence sequence = new Sequence(nomSequence);
                sequence.setRepetition(nbRepetitions);

                //Ajout de la description et du temps de Repos Long
                if(strTempsReposLong.isEmpty()){
                    sequence.setTempsReposLong(60);
                }else{
                    sequence.setTempsReposLong(Integer.parseInt(strTempsReposLong));
                }
                sequence.setDescription(description);

                // enregistrement de sequence en bdd
                long sequenceId = mDb.getAppDatabase()
                        .sequenceDao()
                        .insert(sequence);

                //enregistrement dans la table commune entre sequence et cycle (many to many)
                ArrayList cycles = new ArrayList<Cycle>();
                for (int i = 0; i < cyclesAvecTravails.size(); i++){
                    SequenceCycleCrossRef sequenceCycleCrossRef = new SequenceCycleCrossRef();
                    sequenceCycleCrossRef.sequenceId = sequenceId;
                    sequenceCycleCrossRef.cycleId = cyclesAvecTravails.get(i).getCycle().getCycleId();
                    mDb.getAppDatabase()
                            .sequenceCycleCrossRefDao()
                            .insert(sequenceCycleCrossRef);
                }

                return sequence;
            }

            @Override
            protected void onPostExecute(Sequence sequence) {

                super.onPostExecute(sequence);
                finish();// on stop l'activité après l'ajout en bd
            }
        }

        SaveSequence saveSequence = new SaveSequence();
        saveSequence.execute(); //execution de l'async


    }
}