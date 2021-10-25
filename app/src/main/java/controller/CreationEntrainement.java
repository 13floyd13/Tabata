package controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tabata.R;

public class CreationEntrainement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_entrainement);
        String ajouter = getResources().getString(R.string.ajouter);
        String space = getResources().getString(R.string.space);
        String sequence = getResources().getString(R.string.sequence);
        String ajouterSequence = ajouter+space+sequence;

        Button button = findViewById(R.id.buttonAjouterSequence);
        button.setText(ajouterSequence);

    }

    public void onAjouterSequence(View view) {
    }

    public void onCreerSequence(View view) {
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