package controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tabata.R;

public class MenuSuppression extends AppCompatActivity {

    //Constante
    private static final boolean SUPPRESSION_KEY = true;

    //Views
    private Button bSuppressionEntrainement;
    private Button bSuppressionSequence;
    private Button bSuppressionCycle;
    private Button bSuppressionTravail;

    //Ressources
    private String delete;
    private String entrainement;
    private String sequence;
    private String cycle;
    private String travail;
    private String space;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_supression);

        //récupération des Views
        bSuppressionEntrainement = findViewById(R.id.btnSuppressionEntrainement);
        bSuppressionSequence = findViewById(R.id.btnSuppressionSequence);
        bSuppressionCycle = findViewById(R.id.btnSuppressionCycle);
        bSuppressionTravail = findViewById(R.id.btnSuppressionTravail);

        //Liste des strings à concatener pour set le text des boutons
        delete = getResources().getString(R.string.delete);
        entrainement = getResources().getString(R.string.entrainement);
        sequence = getResources().getString(R.string.sequence);
        cycle = getResources().getString(R.string.cycle);
        travail = getResources().getString(R.string.travail);
        space = " ";

        String deleteEntrainement = delete + space + entrainement;
        String deleteSequence = delete + space + sequence;
        String deleteCycle = delete + space + cycle;
        String deleteTravail = delete + space + travail;

        //Maj des boutons
        bSuppressionEntrainement.setText(deleteEntrainement);
        bSuppressionSequence.setText(deleteSequence);
        bSuppressionCycle.setText(deleteCycle);
        bSuppressionTravail.setText(deleteTravail);
    }

    //méthodes de redirections vers les listes choisies

    public void onSuppressionEntrainement(View view) {
        Intent goToListEntrainement = new Intent(getApplicationContext(), ListeEntrainement.class);
        goToListEntrainement.putExtra("SUPPRESSION_KEY", SUPPRESSION_KEY);
        startActivity(goToListEntrainement);
    }

    public void onSuppressionSequence(View view) {
        Intent goToListSequence = new Intent(getApplicationContext(), ListeSequence.class);
        goToListSequence.putExtra("SUPPRESSION_KEY", SUPPRESSION_KEY);
        startActivity(goToListSequence);
    }

    public void onSuppressionCycle(View view) {
        Intent goToListCycle = new Intent(getApplicationContext(), ListeCycle.class);
        goToListCycle.putExtra("SUPPRESSION_KEY", SUPPRESSION_KEY);
        startActivity(goToListCycle);
    }

    public void onSuppressionTravail(View view) {
        Intent goToListTravail= new Intent(getApplicationContext(), ListeTravail.class);
        goToListTravail.putExtra("SUPPRESSION_KEY", SUPPRESSION_KEY);
        startActivity(goToListTravail);
    }
}