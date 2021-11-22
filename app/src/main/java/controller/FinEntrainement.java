package controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.tabata.R;

import java.util.List;

import data.AppDatabase;
import data.DatabaseClient;
import modele.Cycle;
import modele.Historique;

public class FinEntrainement extends AppCompatActivity {

    //Data
    private DatabaseClient mDb;

    //Attributs
    private String nomEntrainement;
    private String currentDate;
    private FinEntrainement activity;


    //Ressources

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin_entrainement);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            nomEntrainement = extras.getString("nomEntrainement");
            currentDate = extras.getString("currentDate");
        }

        // Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext());

        this.activity = this;
        AlertDialog.Builder popup = new AlertDialog.Builder(activity);
        popup.setTitle("Fin Entrainement : " + nomEntrainement);
        popup.setMessage("Souhaitez vous ajouter cet entrainement à votre historique?");

        popup.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //méthode intermédiaire car sinon le clique sur le oui lance deux fois l'insert
                getOui();
            }
        });

        popup.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                retourMenu();
            }
        });

        popup.show();

    }

    public void enregistrerEntrainement(){

        class InsertHistoriqueAsync extends android.os.AsyncTask<Void, Void, Historique>{

            @Override
            protected Historique doInBackground(Void... voids) {

                //création de l'objet Historique qui contient le nom de l'entrainement et la date
                Historique historique = new Historique(nomEntrainement, currentDate);

                Long historiqueId = mDb.getAppDatabase()
                        .historiqueDao()
                        .insert(historique);

                return historique;
            }

            @Override
            protected void onPostExecute(Historique historique){
                super.onPostExecute(historique);
                retourMenu();
            }
        }

        //lancement de la classe Async
        InsertHistoriqueAsync insertHistoriqueAsync = new InsertHistoriqueAsync();
        insertHistoriqueAsync.execute();
    }

    //retour au menu en supprimant toutes les activités precedentes
    public void retourMenu(){
        Intent goBackToMain = new Intent(getApplicationContext(), MainActivity.class);
        goBackToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(goBackToMain);
    }

    public void getOui(){
        enregistrerEntrainement();
    }
}