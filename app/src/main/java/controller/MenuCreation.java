package controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tabata.R;

public class MenuCreation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_creation);

        //récupération des Views
        Button bCreationEntrainement = findViewById(R.id.btnCreationEntrainement);
        Button bCreationSequence = findViewById(R.id.btnCreationSequence);
        Button bCreationCycle = findViewById(R.id.btnCreationCycle);
        Button bCreationTravail = findViewById(R.id.btnCreationTravail);

        //Liste des strings à concatener pour set le text des boutons
        String create = getResources().getString(R.string.creer);
        String entrainement = getResources().getString(R.string.entrainement);
        String sequence = getResources().getString(R.string.sequence);
        String cycle = getResources().getString(R.string.cycle);
        String travail = getResources().getString(R.string.travail);
        String space = " ";
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