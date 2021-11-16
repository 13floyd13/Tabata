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
import modele.Entrainement;
import modele.EntrainementAvecSequences;
import modele.EntrainementSequenceCrossRef;
import modele.Sequence;
import modele.SequenceAvecCycles;
import modele.SequenceListAdapter;

public class CreationEntrainement extends AppCompatActivity {


    private static final boolean ENTRAINEMENT_KEY = true;
    private String nomEntrainement;
    private DatabaseClient mDb;
    int tempsPreparation;
    int tempsReposLong;
    private ArrayList<SequenceAvecCycles> sequenceAvecCycles = new ArrayList<SequenceAvecCycles>();
    private ListView listSequenceClicked;
    private SequenceListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_entrainement);

        //récupération des sequences ajoutés
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sequenceAvecCycles = extras.getParcelableArrayList("arrayListSequenceClicked");
        }

        //Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext());

        //On récupère des strings en ressources à concatener
        String ajouter = getResources().getString(R.string.ajouter);
        String space = " ";
        String sequence = getResources().getString(R.string.sequence);
        String ajouterSequence = ajouter+space+sequence;
        String create = getResources().getString(R.string.creer);
        String createSequence = create+space+sequence;

        //récupération du bouton d'ajout de séquence pour ajouter le texte concaténé
        Button buttonAddSequence = findViewById(R.id.buttonAjouterSequence);
        buttonAddSequence.setText(ajouterSequence);

        //récupération du bouton de création de séquence pour ajouter le texte concaténé
        Button buttonCreateSequence = findViewById(R.id.buttonCreerSequence);
        buttonCreateSequence.setText(createSequence);

        //récupération de la listView de sequences ajoutés
        listSequenceClicked = findViewById(R.id.listSequence);

        if (sequenceAvecCycles != null && !sequenceAvecCycles.isEmpty()){

            //Liaison de l'adapter au listView
            adapter = new SequenceListAdapter(this, new ArrayList<SequenceAvecCycles>());
            adapter.clear();
            adapter.addAll(sequenceAvecCycles);
            listSequenceClicked.setAdapter(adapter);
        }
    }

    public void onAjouterSequence(View view) {
        Intent goToListSequence = new Intent(getApplicationContext(), ListeSequence.class);
        goToListSequence.putExtra("ENTRAINEMENT_KEY", ENTRAINEMENT_KEY);

        //envoie des sequences déja ajoutés au préalable
        goToListSequence.putParcelableArrayListExtra("arrayListSequences", sequenceAvecCycles);
        startActivity(goToListSequence);

    }

    public void onCreerSequence(View view) {
        Intent goToCreateSequence = new Intent(getApplicationContext(), CreationSequence.class);
        startActivity(goToCreateSequence);
    }

    public void onSave(View view) {

        //récupération du nom de l'entrainement
        EditText eNomEntrainement = findViewById(R.id.nomEntrainement);
        this.nomEntrainement = eNomEntrainement.getText().toString();

        //On vérifie que le nom de l'entrainement de soit pas vide
        if (this.nomEntrainement.isEmpty()) {
            Toast toast = Toast.makeText(CreationEntrainement.this, "Nom d'entrainement' obligatoire", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 20, 30);
            toast.show();
            return;
        }

        //récupération de la liste des séquences ajoutées
        ListView listViewSequence = findViewById(R.id.listSequence);

        //on vérifie qu'au moins une séquence soit ajoutée
        if (listViewSequence.getCount() == 0) {
            Toast toast = Toast.makeText(CreationEntrainement.this, "Vous devez ajouter au moins une séquence", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 20, 30);
            toast.show();
            return;
        }

        //initialisation d'une List de l'objet Sequence
        /*for (int i = 0; i < listViewSequence.getCount(); i++) {

            Sequence sequence = (Sequence) listViewSequence.getItemAtPosition(i);
            //ajout de la sequence dans une liste
            this.listSequence.add(sequence);
        }*/

        //récupération du temps de préparation
        EditText eTempsPreparation = findViewById(R.id.tempsPreparation);
        String strTempsPreparation = eTempsPreparation.getText().toString();

        //on vérifie que le temps de préparation soit rempli sinon on le met à 10 secondes par défaut
        if(strTempsPreparation.isEmpty()){
            tempsPreparation = 10;
        }else{
            tempsPreparation = Integer.parseInt(strTempsPreparation);
        }

        //récupération du temps de repos Long
        EditText eTempsReposLong = findViewById(R.id.tempsRepos);
        String strTempsReposLong = eTempsReposLong.getText().toString();

        //on vérifie si le temps de repos est rempli sinon on le met à 60 secondes par défaut
        if(strTempsReposLong.isEmpty()){
            tempsReposLong = 60;
        }else{
            tempsReposLong = Integer.parseInt(strTempsReposLong);
        }

        //récupération de la description de l'entrainement
        EditText eDescription = findViewById(R.id.descriptionEntrainement);
        String description = eDescription.getText().toString();


            // Création d'une classe asynchrone pour sauvegarder l'entrainement et les sequences associés dans la base de donnée
            class SaveEntrainement extends AsyncTask<Void, Void, Entrainement> {

                @Override
                protected Entrainement doInBackground(Void... voids) {

                    //Création de l'objet Entrainement
                    Entrainement entrainement = new Entrainement(nomEntrainement);

                    //Ajout de la description et du temps de prépration et repos
                    entrainement.setDescription(description);
                    entrainement.setTempsPreparation(tempsPreparation);
                    entrainement.setTempsRepos(tempsReposLong);


                    // Enregistrement de l'objet en BDD avec la méthode insert du Dao
                    long entrainementId = mDb.getAppDatabase()
                            .entrainementDao()
                            .insert(entrainement);

                    //Enregistrement de la relation entre entrainement et sequence dans la table commune (many to many)
                    for (int i = 0; i < sequenceAvecCycles.size(); i++){
                        EntrainementSequenceCrossRef entrainementSequenceCrossRef = new EntrainementSequenceCrossRef();
                        entrainementSequenceCrossRef.entrainementId = entrainementId;
                        entrainementSequenceCrossRef.sequenceId = sequenceAvecCycles.get(i).getSequence().getSequenceId();
                        mDb.getAppDatabase()
                                .entrainementSequenceCrossRefDao()
                                .insert(entrainementSequenceCrossRef);
                    }

                    return entrainement;
                }
            }


        //appel de la classe asynchrone et execution de l'insertion en base de donnée
        SaveEntrainement saveEntrainement = new SaveEntrainement();
        saveEntrainement.execute();
        finish();
    }
/*
    /* mettre dans OnCreate: Récupération du DatabaseClient
            mDb = DatabaseClient.getInstance(getApplicationContext());
     * Création d'une classe asynchrone pour sauvegarder la tache donnée par l'utilisateur

    class SaveUser extends AsyncTask<Void, Void, User> {

        @Override
        protected User doInBackground(Void... voids) {

            // creating a task
            User user = new User();
            user.setPrenom(sPrenom);
            user.setNom(sNom);

            // adding to database
            mDb.getAppDatabase()
                    .userDao()
                    .insert(user);

            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);

            // Quand la tache est créée, on arrête l'activité AddTaskActivity (on l'enleve de la pile d'activités)
            setResult(RESULT_OK);
            finish();
            Toast.makeText(getApplicationContext(), "Utilisateur " + sPrenom + " " + sNom + " ajouté", Toast.LENGTH_SHORT).show();
        }

    }
    // execution
    SaveUser su = new SaveUser();
        su.execute();
}*/
}