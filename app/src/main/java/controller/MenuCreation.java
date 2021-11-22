package controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tabata.R;

public class MenuCreation extends AppCompatActivity {

    //Views
    private Button bCreationEntrainement;
    private Button bCreationSequence;
    private Button bCreationCycle;
    private Button bCreationTravail;

    //Ressources
    private String create;
    private String entrainement;
    private String sequence;
    private String cycle;
    private String travail;
    private String space;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_creation);

        //récupération des Views
        bCreationEntrainement = findViewById(R.id.btnCreationEntrainement);
        bCreationSequence = findViewById(R.id.btnCreationSequence);
        bCreationCycle = findViewById(R.id.btnCreationCycle);
        bCreationTravail = findViewById(R.id.btnCreationTravail);

        //Liste des strings à concatener pour set le text des boutons
        create = getResources().getString(R.string.creer);
        entrainement = getResources().getString(R.string.entrainement);
        sequence = getResources().getString(R.string.sequence);
        cycle = getResources().getString(R.string.cycle);
        travail = getResources().getString(R.string.travail);
        space = " ";

        String createEntrainement = create + space + entrainement;
        String createSequence = create + space + sequence;
        String createCycle = create + space + cycle;
        String createTravail = create + space + travail;

        //Maj des boutons
        bCreationEntrainement.setText(createEntrainement);
        bCreationSequence.setText(createSequence);
        bCreationCycle.setText(createCycle);
        bCreationTravail.setText(createTravail);

    }

    public void onCreationEntrainement(View view) {
        Intent goToCreationEntrainement = new Intent(getApplicationContext(), CreationEntrainement.class);
        startActivity(goToCreationEntrainement);
    }

    public void onCreationSequence(View view) {
        Intent goToCreationSequence = new Intent(getApplicationContext(), CreationSequence.class);
        startActivity(goToCreationSequence);
    }

    public void onCreationCycle(View view) {
        Intent goToCreationCycle = new Intent(getApplicationContext(), CreationCycle.class);
        startActivity(goToCreationCycle);
    }

    public void onCreationTravail(View view) {
        Intent goToCreationTravail = new Intent(getApplicationContext(), CreationTravail.class);
        startActivity(goToCreationTravail);

    }
}