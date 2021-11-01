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
import modele.Travail;

public class CreationCycle extends AppCompatActivity {

    private String nomCycle;
    private DatabaseClient mDb;
    private List<Travail> travails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_cycle);

        //Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext());

        //On récupère des strings en ressources à concatener
        String ajouter = getResources().getString(R.string.ajouter);
        String space = " ";
        String create = getResources().getString(R.string.creer);
        String travail = getResources().getString(R.string.travail);

        String strCreateTravail = create+space+travail;
        String strAjouterTravail = ajouter+space+travail;

        //récupértion du bouton d'ajout de cycle pour ajouter la string
        Button buttonAddTravail = findViewById(R.id.buttonAjouterTravail);
        buttonAddTravail.setText(strAjouterTravail);

        //récupération du bouton de création de cycle pour ajouter la string
        Button buttonCreateTravail = findViewById(R.id.buttonCreerTravail);
        buttonCreateTravail.setText(strCreateTravail);
    }



    public void onAjouterTravail(View view) {
    }

    public void onCreerTravail(View view) {
    }

    public void onSave(View view) {

        //récupération du nom du cycle
        EditText eNomCycle = findViewById(R.id.nomCycle);
        nomCycle = eNomCycle.getText().toString();

        //on vérifie que le nom du cycle ne soit pas vide
        if (nomCycle.isEmpty()){
            Toast toast = Toast.makeText(CreationCycle.this, "Nom de cycle obligatoire", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 20, 30);
            toast.show();
            return;
        }

        //récupération de la liste de travails ajoutées
        ListView listViewTravail = findViewById(R.id.listTravails);

        //on vérifie qu'au moins un travail soit ajouté
        if (listViewTravail.getCount() == 0){
            Toast toast = Toast.makeText(CreationCycle.this, "Vous devez ajouter au moins un travail", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 20, 30);
            toast.show();
            return;
        }

        //remplissage de la List de Travail
        for (int i = 0; i < listViewTravail.getCount(); i++){

            Travail travail = (Travail) listViewTravail.getItemAtPosition(i);
            travails.add(travail);
        }

        //Création classe asynchrone pour sauvegarder le cycle
        class SaveCycle extends AsyncTask<Void, Void, Cycle>{


            @Override
            protected Cycle doInBackground(Void... voids) {

                //Création de l'objet Cycle
                Cycle cycle = new Cycle(nomCycle);

                //enregistrement du cycle en bdd
                mDb.getAppDatabase()
                        .cycleDao()
                        .insert(cycle);

                //enregistrements des travails dans la table cycle
                mDb.getAppDatabase()
                        .cycleDao()
                        .insertTravails(travails);

                return cycle;
            }
        }

        SaveCycle saveCycle = new SaveCycle();
        saveCycle.execute();

    }















}