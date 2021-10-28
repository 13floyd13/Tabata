package controller;

import androidx.appcompat.app.AppCompatActivity;

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
import modele.Sequence;

public class CreationEntrainement extends AppCompatActivity {

    String nomEntrainement;
    DatabaseClient mDb;
    List<Sequence> listSequence;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_entrainement);


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




    }

    public void onAjouterSequence(View view) {

    }

    public void onCreerSequence(View view) {
    }

    public void onSave(View view) {
        //récupération du nom de la séquence
        EditText eNomEntrainement = findViewById(R.id.nomEntrainement);
        this.nomEntrainement = eNomEntrainement.getText().toString();
        Entrainement entrainement = new Entrainement(this.nomEntrainement);

        //récupération de la liste des séquences ajoutées
        ListView listViewSequence = findViewById(R.id.listSequence);

        //initialisation d'une List de l'objet Sequence
        for (int i = 0; i < listViewSequence.getCount(); i++) {

            Sequence sequence = (Sequence) listViewSequence.getItemAtPosition(i);
            //ajout de la sequence dans une liste
            listSequence.add(sequence);
        }


        //On vérifie que le nom de la séquence de soit pas vide
        if (this.nomEntrainement.isEmpty()) {
            Toast toast = Toast.makeText(CreationEntrainement.this, "Nom de séquence obligatoire", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 20, 30);
            toast.show();

            //on vérifie qu'au moins une séquence soit ajoutée
        } else if (listViewSequence.getCount() == 0) {
            Toast toast = Toast.makeText(CreationEntrainement.this, "Vous devez ajouter au moins une séquence", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 20, 30);
            toast.show();

            // sauvegarder
        } else {




        }


        class SaveEntrainement extends AsyncTask<Void, Void, EntrainementAvecSequences> {

            @Override
            protected EntrainementAvecSequences doInBackground(Void... voids) {

                // Création de l'entrainementAvecSequence
                EntrainementAvecSequences entrainementAvecSequences = new EntrainementAvecSequences(entrainement, listSequence);

                // Enregistrement de l'objet en BDD
                mDb.getAppDatabase()
                        .EntrainementDao()
                        .insert(eAvecS);

                return;
            }

            @Override
            protected onPostExecute() {
                // nothing
            }
        }
        SaveEntrainement saveEntrainement = new SaveEntrainement();
        saveEntrainement.execute();
        //new SaveEntrainement.execute(entrainementAvecSequences);
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