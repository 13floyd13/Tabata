package controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabata.R;

import data.DatabaseClient;
import modele.Travail;

public class CreationTravail extends AppCompatActivity {

    //Attributs
    private String nomTravail;
    private int tempsTravail;
    private int tempsRepos;

    //database
    private DatabaseClient mDb;

    //Views
    private EditText eTempsTravail;
    private EditText eTempsRepos;
    private TextView tvTempsTravail;
    private TextView tvTempsRepos;
    private EditText eNomTravail;

    //Ressources
    private String temps;
    private String space;
    private String travail;
    private String repos;
    private String travailObligatoire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_travail);

        //Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext());

        //On récupère des strings en ressources à concatener
        temps = getResources().getString(R.string.temps);
        space = " ";
        travail = getResources().getString(R.string.travail);
        repos = getResources().getString(R.string.repos);
        travailObligatoire = getResources().getString(R.string.travailObligatoire);

        String strTempsTravail = temps+space+travail;
        String strTempsRepos = temps+space+repos;

        //récupération du TextView de temps de travail pour ajouter la string
        tvTempsTravail = findViewById(R.id.textTempsTravail);
        tvTempsTravail.setText(strTempsTravail);

        //récupération du TextView de temps de repos pour ajouter la string
        tvTempsRepos = findViewById(R.id.textTempsRepos);
        tvTempsRepos.setText(strTempsRepos);

    }

    public void onSave(View view) {
        // on récupère le nom
        eNomTravail = findViewById(R.id.nomTravail);
        nomTravail = eNomTravail.getText().toString();

        //on vérifie qu'il n'est pas vide
        if(nomTravail.isEmpty()){
            Toast toast = Toast.makeText(CreationTravail.this, travailObligatoire, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 20, 30);
            toast.show();
            return;
        }

        //récupération des EditText
        eTempsTravail = findViewById(R.id.tempsTravail);
        eTempsRepos = findViewById(R.id.tempsRepos);

        //on récupère le temps de travail
        String strTempsTravail = eTempsTravail.getText().toString();

        //on vérifie s'il est vide sinon on l'initialise à 20 secondes par défaut
        if(strTempsTravail.isEmpty()){
            tempsTravail = 20;
        }else{
            tempsTravail = Integer.parseInt(strTempsTravail);
        }

        //on récupère le temps de repos
        String strTempsRepos = eTempsRepos.getText().toString();

        //on vérifie s'il est vide sinon on l'initialise à 10 secondes par défaut
        if(strTempsRepos.isEmpty()){
            tempsRepos = 10;
        }else{
            tempsRepos = Integer.parseInt(strTempsRepos);
        }

        //Création d'une classe asynchrone pour sauvegarder le Travail
        class SaveTravail extends AsyncTask<Void, Void, Travail>{

            @Override
            protected Travail doInBackground(Void... voids) {

                //Création de l'objet Travail
                Travail travail = new Travail(nomTravail,tempsTravail,tempsRepos);

                //Enregistrement de l'objet en BDD avec la méthode insert du Dao
                Long travailId = mDb.getAppDatabase()
                        .travailDao()
                        .insert(travail);
                
                return travail;

            }

            @Override
            protected void onPostExecute(Travail travail) {

                super.onPostExecute(travail);
                finish(); // on stop l'activité après l'ajout en bd
            }
        }

        SaveTravail saveTravail = new SaveTravail();
        saveTravail.execute(); //execution de l'async

    }
}