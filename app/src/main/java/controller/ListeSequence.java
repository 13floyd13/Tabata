package controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabata.R;

import java.util.ArrayList;
import java.util.List;

import data.AppDatabase;
import data.DatabaseClient;
import modele.Cycle;
import modele.CycleAvecTravails;
import modele.CycleListAdapter;
import modele.Entrainement;
import modele.EntrainementAvecSequences;
import modele.Sequence;
import modele.SequenceAvecCycles;
import modele.SequenceListAdapter;

public class ListeSequence extends AppCompatActivity {

    //Data
    private AppDatabase mDb;
    private SequenceListAdapter adapter;

    //Attributs
    private ArrayList<SequenceAvecCycles> sequences = new ArrayList<SequenceAvecCycles>();
    private boolean suppression = false;
    private ArrayList<Long> sequencesAjoutes = new ArrayList<>();
    private String nomEntrainement;
    private ArrayList<Entrainement> entrainementAsupprimer = new ArrayList<>();
    private ListeSequence activity;
    private AlertDialog.Builder popup1;
    private AlertDialog.Builder popup2;
    private Sequence sequenceASupprimer;
    private ArrayList<Integer> repets = new ArrayList<Integer>();

    //Views
    private ListView listSequence;
    private TextView titrePage;

    //ressources
    private String liste;
    private String space;
    private String sequence;
    private String strSuppression;
    private String strsuppressionSequence;
    private String strSuppressionSupplementaire;
    private String oui;
    private String non;
    private String strCycle;
    private String strSequence;
    private String strEntrainement;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_sequence);


        this.activity = this;

        //pr??paration de la popup
        popup1 = new AlertDialog.Builder(activity);
        popup2 = new AlertDialog.Builder(activity);


        //r??cup??ration des Intents
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sequences = extras.getParcelableArrayList("arrayListSequences");
            suppression = extras.getBoolean("SUPPRESSION_KEY");
            nomEntrainement = extras.getString("nomEntrainement");
            repets = (ArrayList<Integer>) getIntent().getSerializableExtra("repets");
        }

        //on r??cup??re les strings ?? concat??ner
        strSuppression = getResources().getString(R.string.suppression);
        strsuppressionSequence = getResources().getString(R.string.suppressionSequence);
        strSuppressionSupplementaire = getResources().getString(R.string.suppressionSupplementaire);
        strCycle = getResources().getString(R.string.cycle);
        strSequence = getResources().getString(R.string.sequence);
        strEntrainement = getResources().getString(R.string.entrainement);
        oui = getResources().getString(R.string.oui);
        non = getResources().getString(R.string.non);
        liste = getResources().getString(R.string.liste);
        space = " ";
        sequence = getResources().getString(R.string.sequence);
        String strListeSequence = liste + space + sequence;

        //r??cup??ration du TextView pour ajouter la string
        titrePage = findViewById(R.id.titrePage);
        titrePage.setText(strListeSequence);

        //r??cup??ration du ListView
        listSequence = findViewById(R.id.listSequence);

        // R??cup??ration du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();

        //Liaison de l'adapder au listView
        adapter = new SequenceListAdapter(this, new ArrayList<SequenceAvecCycles>());
        listSequence.setAdapter(adapter);

        //si on vient du menu Supression
        if (suppression) {

            listSequence.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //r??cup??ration de l'objet cliqu??
                    SequenceAvecCycles sequenceAvecCyclesClicked = adapter.getItem(position);
                    sequenceASupprimer = sequenceAvecCyclesClicked.getSequence();

                    //r??cup??ration des sequences qui contiennent ce cycle
                    getEntrainementsAssocies(sequenceASupprimer);

                    //mise en place de la popup de validation de la suppression de la sequence
                    popup1.setTitle(strSuppression);
                    popup1.setMessage(strsuppressionSequence + space + sequenceASupprimer.getNom());

                    popup1.setPositiveButton(oui, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //on regarde si on aura des entrainements qui deviendront vides et devront donc ??tre supprim??es
                            //cela demandera une deuxi??me validation ?? l'utilisateur
                            if(!entrainementAsupprimer.isEmpty()){

                                askSuppression();

                                //sinon on supprime juste la s??quence
                            }else{

                                suppression();
                            }

                            //fermeture de la popup
                            dialog.dismiss();
                        }
                    });

                    popup1.setNegativeButton(non, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            finish();
                        }
                    });

                    popup1.show();
                }
            });

            //si on vient du menu Creation
        }else {

            //ajout d'un ??venement click ?? la listeView
            listSequence.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //R??cup??ration de la s??quence cliqu??
                    SequenceAvecCycles sequenceClicked = adapter.getItem(position);

                    //boucle pour r??cuperer les id des sequences d??ja ajout??s ?? la creation d'entrainement
                    for (int i = 0; i < sequences.size(); i++){
                        sequencesAjoutes.add(sequences.get(i).getSequence().getSequenceId());
                    }

                    //on v??rifie que la sequence n'a pas d??ja ??t?? cliqu??
                    if (sequencesAjoutes.contains(sequenceClicked.getSequence().getSequenceId())) {

                        Toast toast = Toast.makeText(ListeSequence.this, "Sequence d??ja ajout??", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER, 20, 30);
                        toast.show();

                        //si pas ajout?? on l'ajoute ?? la creation d'entrainement
                    }else {

                        Intent goBacktoEntrainement = new Intent(getApplicationContext(), CreationEntrainement.class);
                        sequences.add(sequenceClicked);
                        repets.add(sequenceClicked.getNbRepet());
                        goBacktoEntrainement.putIntegerArrayListExtra("repets", repets);
                        goBacktoEntrainement.putExtra("nbRepet", sequenceClicked.getNbRepet());
                        goBacktoEntrainement.putParcelableArrayListExtra("arrayListSequenceClicked", sequences);
                        goBacktoEntrainement.putExtra("nomEntrainement", nomEntrainement);
                        goBacktoEntrainement.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(goBacktoEntrainement);
                    }
                }
            });
        }
    }

    //r??cup??ration des sequences en bd
    private void getSequences(){

        class RecupererSequenceAsync extends android.os.AsyncTask<Void, Void, List<SequenceAvecCycles>>{

            @Override
            protected List<SequenceAvecCycles> doInBackground(Void... voids) {
                List<SequenceAvecCycles> sequenceList = mDb
                        .sequenceDao()
                        .getAll();
                return sequenceList;
            }

            @Override
            protected void onPostExecute(List<SequenceAvecCycles> sequencesList){
                super.onPostExecute(sequencesList);

                //Mise ?? jour de l'adapter avec la liste de sequences
                adapter.clear();
                adapter.addAll(sequencesList);
                adapter.notifyDataSetChanged();
            }
        }

        //execution de la demande asynchrone
        RecupererSequenceAsync recup = new RecupererSequenceAsync();
        recup.execute();
    }

    //r??cup??rations de tous les entrainements contenant la sequence qui sera supprim??
    public void getEntrainementsAssocies(Sequence sequenceQuiSeraSupprime){

        class RecupererEntrainementsAssocies extends android.os.AsyncTask<Void, Void, List<Entrainement>>{

            @Override
            protected List<Entrainement> doInBackground(Void... voids) {

                List<Long> entrainementsIds = mDb
                        .entrainementSequenceCrossRefDao()
                        .getEntrainementsId(sequenceQuiSeraSupprime.getSequenceId());

                List<Entrainement> entrainements = mDb
                        .entrainementDao()
                        .getEntrainements(entrainementsIds);

                return entrainements;
            }

            @Override
            protected void onPostExecute(List<Entrainement> entrainements) {
                super.onPostExecute(entrainements);

                //Pour chaque entrainement trouv??
                //on va regarder s'il ne contient qu'une sequence ou non
                for (int i = 0; i < entrainements.size(); i++){
                    checkEntrainement(entrainements.get(i));
                }
            }
        }

        RecupererEntrainementsAssocies recupererEntrainementsAssocies = new RecupererEntrainementsAssocies();
        recupererEntrainementsAssocies.execute();
    }

    //On va regarder sur les entrainements contenant la sequences ?? supprimer
    //s'ils contiennent d'autres sequences ou non
    //s'ils n'ont pas d'autre sequence, il faudra alors les ajouter ?? une liste d'entrainement ?? supprimer
    public void checkEntrainement(Entrainement entrainement){

        class RecupererEntrainementADelete extends android.os.AsyncTask<Void, Void, Integer>{

            @Override
            protected Integer doInBackground(Void... voids) {

                int nbSequences = mDb
                        .entrainementSequenceCrossRefDao()
                        .getNbSequences(entrainement.getEntrainementId());

                return nbSequences;
            }

            @Override
            protected void onPostExecute(Integer nbSequences){
                super.onPostExecute(nbSequences);

                //Si l'entrainement ne contient qu'une sequence (la sequence qui sera supprim??)
                //on ajoute cet entrainement ?? une liste
                if (nbSequences == 1){
                    entrainementAsupprimer.add(entrainement);
                }
            }
        }

        RecupererEntrainementADelete recupererEntrainementADelete = new RecupererEntrainementADelete();
        recupererEntrainementADelete.execute();

    }

    //methode appel??e uniquement si on a au moins un entrainement ?? supprimer en plus de la sequence
    public void askSuppression(){

        //pr??paration du message qui va contenir les noms des entrainements ?? supprimer
        String space = " ";
        String message = strSuppressionSupplementaire + "\n";

        message += strEntrainement + space;

        //r??cup??ration des noms des entrainements ?? supprimer
        for (int i = 0; i < entrainementAsupprimer.size(); i++){

            message += entrainementAsupprimer.get(i).getNom() + space;

        }

        //on pr??pare une popup de confirmation de suppression
        // des elements en plus de la s??quence cliqu??
        popup2.setTitle(strSuppression);
        popup2.setMessage(message);

        popup2.setPositiveButton(oui, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //si oui on lance la suppression
                suppression();
            }
        });

        popup2.setNegativeButton(non, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //sinon on stop l'activit?? et on ne supprimera pas le travail non plus
                finish();
            }
        });

        popup2.show();
    }

    //m??thode qui va supprimer tout ce qu'on a besoin
    public void suppression(){

        class SupprimerToutAsync extends android.os.AsyncTask<Void, Void, Void> {

            //suppression des objets de la bd
            @Override
            protected Void doInBackground(Void... voids) {

                mDb.sequenceDao()
                        .delete(sequenceASupprimer);

                mDb.entrainementDao()
                        .deleteAll(entrainementAsupprimer);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
                finish();
            }
        }

        SupprimerToutAsync supprimerTravailAsync = new SupprimerToutAsync();
        supprimerTravailAsync.execute();
    }

    @Override
    protected void onStart(){
        super.onStart();

        //mise ?? jour des sequences
        getSequences();
    }
}



