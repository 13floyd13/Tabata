package controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tabata.R;

public class MenuSuppression extends AppCompatActivity {

    private static final boolean SUPPRESSION_KEY = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_supression);
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