package controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.tabata.R;

import java.util.ArrayList;
import java.util.List;

import data.AppDatabase;
import modele.Cycle;
import modele.CycleAvecTravails;
import modele.Entrainement;
import modele.EntrainementAvecSequences;
import modele.Sequence;
import modele.Travail;

public class Play extends AppCompatActivity {

    private AppDatabase mDb;
    private Entrainement entrainement;
    private List<Sequence> sequences;
    private List<Cycle> cycles;
    private List<Travail> travails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Bundle extras = getIntent().getExtras();
        if (extras != null){

            EntrainementAvecSequences entrainementAvecSequences = extras.getParcelable("entrainementAvecSequences");
            entrainement = entrainementAvecSequences.getEntrainement();
            sequences = entrainementAvecSequences.getSequences();

        }
    }

    public void getCycles() {

        class GetEntrainementAsync extends android.os.AsyncTask<Void, Void, Cycle>{

            @Override
            protected Cycle doInBackground(Void... voids) {
                //Entrainement train = mDb;
                return null;

            }
        }
    }
}