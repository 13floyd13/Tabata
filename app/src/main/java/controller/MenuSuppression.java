package controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tabata.R;

public class MenuSuppression extends AppCompatActivity {

    private static final boolean SUPPRESSION_KEY = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_supression);

        //récupération des Views
        Button bSuppressionEntrainement = findViewById(R.id.btnSuppressionEntrainement);
        Button bSuppressionSequence = findViewById(R.id.btnSuppressionSequence);
        Button bSuppressionCycle = findViewById(R.id.btnSuppressionCycle);
        Button bSuppressionTravail = findViewById(R.id.btnSuppressionTravail);

        //Liste des strings à concatener pour set le text des boutons
        String delete = getResources().getString(R.string.delete);
        String entrainement = getResources().getString(R.string.entrainement);
        String sequence = getResources().getString(R.string.sequence);
        String cycle = getResources().getString(R.string.cycle);
        String travail = getResources().getString(R.string.travail);
        String space = " ";
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