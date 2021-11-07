package controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tabata.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onChoixEntrainement(View view) {

        Intent goToChoixEntrainement = new Intent(getApplicationContext(), ListeCycle.class);
        startActivity(goToChoixEntrainement);
    }

    public void onCreationEntrainement(View view) {
        Intent goToCreationEntrainement = new Intent(getApplicationContext(), CreationSequence.class);
        startActivity(goToCreationEntrainement);
    }

    public void onHistorique(View view) {
    }
}