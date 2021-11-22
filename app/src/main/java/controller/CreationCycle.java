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
import modele.CycleTravailCrossRef;
import modele.Sequence;
import modele.Travail;
import modele.TravailListAdapter;

public class CreationCycle extends AppCompatActivity {

    //Data
    private DatabaseClient mDb;
    private TravailListAdapter adapter;

    //Attributs
    private String nomCycle;
    private int nbRepetitions;
    private ArrayList<Travail> travails = new ArrayList<Travail>();
    private String nbRepet;

    //Views
    private ListView listTravailClicked;
    private Button buttonAddTravail;
    private Button buttonCreateTravail;
    private EditText eNomCycle;
    private EditText eNbRepetitions;
    private ListView listViewTravail;

    //Ressources
    private String ajouter;
    private String space;
    private String create;
    private String travail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_cycle);


        //récupération des travails ajoutés
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            travails = extras.getParcelableArrayList("arrayListTravailsClicked");
            nomCycle = extras.getString("nomCycle");
            nbRepet = extras.getString("nbRepet");
        }

        if(nomCycle != null){
            eNomCycle = findViewById(R.id.nomCycle);
            eNomCycle.setText(nomCycle);
        }

        if(nbRepet != null){
            eNbRepetitions = findViewById(R.id.nbRepetitions);
            eNbRepetitions.setText(nbRepet);
        }

        //Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext());

        //On récupère des strings en ressources à concatener
        ajouter = getResources().getString(R.string.ajouter);
        space = " ";
        create = getResources().getString(R.string.creer);
        travail = getResources().getString(R.string.travail);

        String strCreateTravail = create+space+travail;
        String strAjouterTravail = ajouter+space+travail;

        //récupértion du bouton d'ajout de cycle pour ajouter la string
        buttonAddTravail = findViewById(R.id.buttonAjouterTravail);
        buttonAddTravail.setText(strAjouterTravail);

        //récupération du bouton de création de cycle pour ajouter la string
        buttonCreateTravail = findViewById(R.id.buttonCreerTravail);
        buttonCreateTravail.setText(strCreateTravail);

        //récupération de la listView de travail ajoutés
        listTravailClicked = findViewById(R.id.listTravails);

        if (travails != null && !travails.isEmpty()){
            //Liaison à l'adapter au listView
            adapter = new TravailListAdapter(this, new ArrayList<Travail>());
            adapter.clear();
            adapter.addAll(travails);
            listTravailClicked.setAdapter(adapter);
        }

    }

    public void onAjouterTravail(View view) {

        //on récupère les editText s'ils ont déja été remplis pour les conserver entre le changement d'activité
        eNomCycle = findViewById(R.id.nomCycle);
        nomCycle = eNomCycle.getText().toString();

        eNbRepetitions = findViewById(R.id.nbRepetitions);
        nbRepet = eNbRepetitions.getText().toString();

        Intent goToListeTravail = new Intent(getApplicationContext(), ListeTravail.class);

        //envoie de la liste de travails déja ajouté
        goToListeTravail.putParcelableArrayListExtra("arrayListTravails", travails);
        goToListeTravail.putExtra("nomCycle", nomCycle);
        goToListeTravail.putExtra("nbRepet", nbRepet);
        startActivity(goToListeTravail);
    }

    public void onCreerTravail(View view) {
        Intent goToCreateTravail = new Intent(getApplicationContext(), CreationTravail.class);
        startActivity(goToCreateTravail);
    }

    public void onSave(View view) {

        //récupération du nom du cycle
        eNomCycle = findViewById(R.id.nomCycle);
        nomCycle = eNomCycle.getText().toString();

        //récupération du nombre de répétitions
        eNbRepetitions = findViewById(R.id.nbRepetitions);
        nbRepet = eNbRepetitions.getText().toString();

        // on s'assure que l'editText ne soit pas vide
        if (nbRepet.isEmpty()){
            nbRepetitions = 4;
        }else {
            nbRepetitions = Integer.parseInt(nbRepet);
        }

        //on vérifie que le nom du cycle ne soit pas vide
        if (nomCycle.isEmpty()){
            Toast toast = Toast.makeText(CreationCycle.this, "Nom de cycle obligatoire", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 20, 30);
            toast.show();
            return;
        }

        //récupération de la liste de travails ajoutées
        listViewTravail = findViewById(R.id.listTravails);

        //on vérifie qu'au moins un travail soit ajouté
        if (listViewTravail.getCount() == 0){
            Toast toast = Toast.makeText(CreationCycle.this, "Vous devez ajouter au moins un travail", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 20, 30);
            toast.show();
            return;
        }


        //Création classe asynchrone pour sauvegarder le cycle
        class SaveCycle extends AsyncTask<Void, Void, Cycle>{


            @Override
            protected Cycle doInBackground(Void... voids) {

                //Création de l'objet Cycle
                Cycle cycle = new Cycle(nomCycle);
                cycle.setRepetition(nbRepetitions);

                //enregistrement du cycle en bdd
                long cycleId = mDb.getAppDatabase()
                        .cycleDao()
                        .insert(cycle);


                //mise à jour de la base commune entre cycle et travail (many to many)
                for (int i = 0; i < travails.size(); i++){
                    CycleTravailCrossRef cycleTravailCrossRef = new CycleTravailCrossRef();
                    cycleTravailCrossRef.cycleId = cycleId;
                    cycleTravailCrossRef.travailId = travails.get(i).getTravailId();
                    mDb.getAppDatabase()
                            .cycleTravailCrossRefDao()
                            .insert(cycleTravailCrossRef);
                }

                return cycle;
            }

            @Override
            protected void onPostExecute(Cycle cycle) {

                super.onPostExecute(cycle);
                finish();// on stop l'activité après l'ajout en bd
            }
        }

        SaveCycle saveCycle = new SaveCycle();
        saveCycle.execute();

    }

}