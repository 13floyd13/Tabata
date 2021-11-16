package controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tabata.R;

public class MenuCreation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_creation);
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