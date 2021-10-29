
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

import java.util.List;

import data.DatabaseClient;
import modele.Cycle;
import modele.Sequence;

public class CreationSequence extends AppCompatActivity {

    private String nomSequence;
    private DatabaseClient mDb;
    private List<Cycle> listeCycle;
    int tempsReposLong;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_sequence);

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
    }

    public void onAjouterCycle(View view) {
    }

    public void onCreerCycle(View view) {
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

        //on vérigie qu'au moins un cycle soit ajoutée
        if (listViewCycle.getCount() == 0){
            Toast toast = Toast.makeText(CreationSequence.this, "Vous devez ajouter au moins un cycle", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 20, 30);
            toast.show();
            return;
        }

        //remplissage de la List de Cycle
        for (int i = 0; i < listViewCycle.getCount(); i++){

        Cycle cycle = (Cycle) listViewCycle.getItemAtPosition(i);
        listeCycle.add(cycle);
        }

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
                sequence.setDescription(description);
                sequence.setTempsReposLong(tempsReposLong);

                // enregistrement de sequence en bdd
                mDb.getAppDatabase()
                        .sequenceDao()
                        .insert(sequence);

                //enregistrement des cycles dans la table Sequence
                mDb.getAppDatabase()
                        .sequenceDao()
                        .insertCycles(listeCycle);


                return sequence;
            }
        }

        SaveSequence saveSequence = new SaveSequence();
        saveSequence.execute();

    }
}