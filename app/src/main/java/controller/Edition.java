package controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tabata.R;

public class Edition extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edition);
    }

    public void onMenuCreation(View view) {
        Intent goToMenuCreation = new Intent(getApplicationContext(), MenuCreation.class);
        startActivity(goToMenuCreation);
    }

    public void onMenuSuppression(View view) {
        Intent goToMenuSuppression = new Intent(getApplicationContext(), MenuSuppression.class);
        startActivity(goToMenuSuppression);
    }
}