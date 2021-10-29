package controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabata.R;

import data.DatabaseClient;

public class CreationTravail extends AppCompatActivity {

    private String nomTravail;
    private int tempsTravail;
    private int tempsRepos;
    private DatabaseClient mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_travail);

        //Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext());

        //On récupère des strings en ressources à concatener
        String temps = getResources().getString(R.string.temps);
        String space = " ";
        String travail = getResources().getString(R.string.travail);
        String repos = getResources().getString(R.string.repos);

        String strTempsTravail = temps+space+travail;
        String strTempsRepos = temps+space+repos;

        //récupération du TextView de temps de travail pour ajouter la string
        TextView tvTempsTravail = findViewById(R.id.textTempsTravail);
        tvTempsTravail.setText(strTempsTravail);

        //récupération du TextView de temps de repos pour ajouter la string
        TextView tvTempsRepos = findViewById(R.id.textTempsRepos);
        tvTempsRepos.setText(strTempsRepos);
    }

    public void onSave(View view) {
        // on récupère le nom
        EditText eNomTravail = findViewById(R.id.nomTravail);
        nomTravail = eNomTravail.getText().toString();

        //on vérifie qu'il n'est pas vide
        if(nomTravail.isEmpty()){
            Toast toast = Toast.makeText(CreationTravail.this, "Nom de travail obligatoire", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 20, 30);
            toast.show();
            return;
        }

        //on récupère le temps de travail
        EditText eTempsTravail = findViewById(R.id.tempsTravail);
        String strTempsTravail = eTempsTravail.getText().toString();

        //on vérifie s'il est vide sinon on l'initialise à 20 secondes par défaut
        if(strTempsTravail.isEmpty()){
            tempsTravail = 20;
        }else{
            tempsTravail = Integer.parseInt(strTempsTravail);
        }

        //on récupère le temps de repos
        EditText eTempsRepos = findViewById(R.id.tempsRepos);
        String strTempsRepos = eTempsRepos.getText().toString();

        //on vérifie s'il est vide sinon on l'initialise à 10 secondes par défaut
        if(strTempsRepos.isEmpty()){
            tempsRepos = 10;
        }else{
            tempsRepos = Integer.parseInt(strTempsRepos);
        }



    }
}