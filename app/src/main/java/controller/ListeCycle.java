package controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import modele.Sequence;
import modele.SequenceAvecCycles;
import modele.Travail;
import modele.TravailListAdapter;

public class ListeCycle extends AppCompatActivity {

    //Data
    private AppDatabase mDb;
    private CycleListAdapter adapter;

    //Attributs
    private ArrayList<CycleAvecTravails> cycles = new ArrayList<CycleAvecTravails>();
    private ArrayList<Long> cyclesAjoutes = new ArrayList<>();
    private boolean suppression = false;
    private String nbRepet;
    private String nomSequence;
    private String description;
    private String strTempsReposLong;
    private ArrayList<Sequence> sequencesAsupprimer = new ArrayList<>();
    private ArrayList<Entrainement> entrainementAsupprimer = new ArrayList<>();
    private ListeCycle activity;
    private AlertDialog.Builder popup1;
    private AlertDialog.Builder popup2;
    private Cycle cycleASupprimer;
    private ArrayList<Integer> repets = new ArrayList<Integer>();

    //Views
    private ListView listCycle;
    private TextView titrePage;

    //Ressources
    private String liste;
    private String space;
    private String cycle;
    private String strSuppression;
    private String strsuppressionCycle;
    private String strSuppressionSupplementaire;
    private String oui;
    private String non;
    private String strCycle;
    private String strSequence;
    private String strEntrainement;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_cycle);

        this.activity = this;

        //pr??paration de la popup
        popup1 = new AlertDialog.Builder(activity);
        popup2 = new AlertDialog.Builder(activity);

        //r??cup??ration des intents
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            cycles = extras.getParcelableArrayList("arrayListCycles");
            suppression = extras.getBoolean("SUPPRESSION_KEY");
            nbRepet = extras.getString("nbRepet");
            nomSequence = extras.getString("nomSequence");
            description = extras.getString("description");
            strTempsReposLong = extras.getString("strTempsReposLong");
            repets = (ArrayList<Integer>) getIntent().getSerializableExtra("repets");
        }

        //on r??cup??re les strings ?? concat??ner
        strSuppression = getResources().getString(R.string.suppression);
        strsuppressionCycle = getResources().getString(R.string.suppressionCycle);
        strSuppressionSupplementaire = getResources().getString(R.string.suppressionSupplementaire);
        strCycle = getResources().getString(R.string.cycle);
        strSequence = getResources().getString(R.string.sequence);
        strEntrainement = getResources().getString(R.string.entrainement);
        oui = getResources().getString(R.string.oui);
        non = getResources().getString(R.string.non);
        liste = getResources().getString(R.string.liste);
        space = " ";
        cycle = getResources().getString(R.string.cycle);
        String strListeCycle = liste+space+cycle;

        //r??cup??ration du TextView pour ajouter la string
        titrePage = findViewById(R.id.titrePage);
        titrePage.setText(strListeCycle);

        //r??cup??ration du ListView
        listCycle = findViewById(R.id.listCycles);

        // R??cup??ration du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();

        //Liaison de l'adapder au listView
        adapter = new CycleListAdapter(this, new ArrayList<CycleAvecTravails>());
        listCycle.setAdapter(adapter);

        //si on vient du menu Supression
        if (suppression) {

            listCycle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //r??cup??ration de l'objet cliqu??
                    CycleAvecTravails cycleAvecTravailsClicked = adapter.getItem(position);
                    cycleASupprimer= cycleAvecTravailsClicked.getCycle();

                    //r??cup??ration des sequences qui contiennent ce cycle
                    getSequenceAssocies(cycleASupprimer);

                    //mise en place de la popup de validation de la suppression de ce cycle
                    popup1.setTitle(strSuppression);
                    popup1.setMessage(strsuppressionCycle + space + cycleASupprimer.getNom());

                    popup1.setPositiveButton(oui, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //on regarde si on aura des sequences qui deviendront vides et devront donc ??tre supprim??es
                            //cela demandera une deuxi??me validation ?? l'utilisateur
                            if(!sequencesAsupprimer.isEmpty()){
                                askSuppression();

                                //sinon on supprime juste le cycle
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

                    //affichage de la popup
                    popup1.show();

                }
            });

            //si on vient du menu Creation
        }else{

            //ajout d'un ??venement click ?? la listeView
            listCycle.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //R??cup??ration du travail cliqu?? pour l'envoyer ?? la cr??ation du cycle dans un arayList de travail
                    CycleAvecTravails cycleClicked = adapter.getItem(position);

                    //boucle pour r??cuperer les id des cycles ajout??s ?? la creation de sequence
                    for(int i = 0; i < cycles.size(); i++){
                        cyclesAjoutes.add(cycles.get(i).getCycle().getCycleId());
                    }

                    //on v??rifie que le cycle n'a pas d??ja ??t?? ajout??
                    if(cyclesAjoutes.contains(cycleClicked.getCycle().getCycleId())){
                        Toast toast = Toast.makeText(ListeCycle.this, "Cycle d??ja ajout??", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER, 20, 30);
                        toast.show();

                        //si pas ajout?? on l'ajoute ?? la creation de sequence
                    }else {

                        Intent goBacktoSequence = new Intent(getApplicationContext(), CreationSequence.class);
                        repets.add(cycleClicked.getNbRepet());
                        cycles.add(cycleClicked);
                        goBacktoSequence.putIntegerArrayListExtra("repets", repets);
                        goBacktoSequence.putParcelableArrayListExtra("arrayListCycleClicked", cycles);
                        goBacktoSequence.putExtra("nomSequence", nomSequence);
                        goBacktoSequence.putExtra("nbRepet", nbRepet);
                        goBacktoSequence.putExtra("strTempsReposLong", strTempsReposLong);
                        goBacktoSequence.putExtra("description", description);
                        goBacktoSequence.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(goBacktoSequence);
                    }
                }
            });
        }
    }

    //r??cup??ration des cycles en bd
    private void getCycles(){

        class RecupererCycleAsync extends android.os.AsyncTask<Void, Void, List<CycleAvecTravails>>{

            @Override
            protected List<CycleAvecTravails> doInBackground(Void... voids) {
                List<CycleAvecTravails> cycleList = mDb
                        .cycleDao()
                        .getAll();
                return cycleList;
            }

            @Override
            protected void onPostExecute(List<CycleAvecTravails> cycles){
                super.onPostExecute(cycles);

                //Mise ?? jour de l'adapter avec la liste des cycles
                adapter.clear();
                adapter.addAll(cycles);
                adapter.notifyDataSetChanged();
            }
        }

        //execution de la demande asynchrone
        RecupererCycleAsync recup = new RecupererCycleAsync();
        recup.execute();
    }


    //on r??cup??re toutes les sequences contenant le cycle qui sera supprim??
    public void getSequenceAssocies(Cycle cycleQuiSeraDelete){

        class RecupererSequenceAssocies extends android.os.AsyncTask<Void, Void, List<Sequence>>{

            @Override
            protected List<Sequence> doInBackground(Void... voids) {
                List<Long> sequencesIds = mDb
                        .sequenceCycleCrossRefDao()
                        .getSequencesId(cycleQuiSeraDelete.getCycleId());

                List<Sequence> sequences = mDb
                        .sequenceDao()
                        .getSequences(sequencesIds);
                return sequences;
            }

            @Override
            protected void onPostExecute(List<Sequence> sequences) {
                super.onPostExecute(sequences);

                for (int i = 0; i < sequences.size(); i++){
                    //on va v??rifier chaque s??quence s'il elle contient suelement le cycle ?? supprimer ou non
                    checkSequence(sequences.get(i));
                }
            }

        }

        RecupererSequenceAssocies recupererSequenceAssocies = new RecupererSequenceAssocies();
        recupererSequenceAssocies.execute();

    }

    //on va regarder le nombre de cycle dans cette sequence
    //si un seul cycle, ce cycle sera celui qui sera supprim??
    //il faudra donc ajouter la sequence dans une list de sequence ?? supprimer
    public void checkSequence(Sequence sequence){

        class RecupererSequenceADelete extends android.os.AsyncTask<Void, Void, Integer>{

            @Override
            protected Integer doInBackground(Void... voids) {

                int nbCycles = mDb
                        .sequenceCycleCrossRefDao()
                        .getNbCycles(sequence.getSequenceId());

                return nbCycles;
            }

            @Override
            protected void onPostExecute(Integer nbCycles){
                super.onPostExecute(nbCycles);

                //si nombre de cycle dans la sequence = 1 on ajoute la sequence ?? la liste de sequence ?? supprimer
                if (nbCycles == 1){
                    sequencesAsupprimer.add(sequence);

                    //on va maintenant r??cup??rer tous les entrainements qui contiennent cette sequence sui sera supprim??
                    getEntrainementsAssocies(sequence);
                }
            }
        }

        RecupererSequenceADelete recupererSequenceADelete = new RecupererSequenceADelete();
        recupererSequenceADelete.execute();

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
    //methode appel??e uniquement si on a au moins une sequence ?? supprimer en plus du cycle
    public void askSuppression(){

        //pr??paration du message qui va contenir les noms des sequences ?? supprimer
        String space = " ";
        String message = strSuppressionSupplementaire + "\n";
        message += strSequence + space;

        //r??cup??ration des noms des sequences ?? supprimer
        for (int i = 0 ; i < sequencesAsupprimer.size(); i++){

            message += sequencesAsupprimer.get(i).getNom() + space;

        }

        //si on a des entrainements ?? supprimer on va les ajouter dans le message
        if (!entrainementAsupprimer.isEmpty()){

            message += "\n";
            message += strEntrainement + space;

            //r??cup??ration des noms des entrainements ?? supprimer
            for (int i = 0; i < entrainementAsupprimer.size(); i++){

                message += entrainementAsupprimer.get(i).getNom() + space;
            }
        }

        //on pr??pare une popup de confirmation de suppression
        // des elements en plus du cycle cliqu??
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

                mDb.cycleDao()
                        .delete(cycleASupprimer);

                mDb.sequenceDao()
                        .deleteAll(sequencesAsupprimer);

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

        //mise ?? jour des cycles
        getCycles();
    }
}