package modele;

import android.os.CountDownTimer;

public class Compteur extends UpdateSource{

    // CONSTANTE
    //private final static long INITIAL_TIME = 5000;
    private static long INITIAL_TIME;

    // DATA
    private long updatedTime = INITIAL_TIME;
    private CountDownTimer timer;   // https://developer.android.com/reference/android/os/CountDownTimer.html
    private String nomSequence;
    private String nomCycle;
    private String nomTravail;
    private Boolean fin = false;



    public Compteur(long time) {
        updatedTime = time*1000;
    }

    // Lancer le compteur
    public void start() {

        if (timer == null) {

            // Créer le CountDownTimer
            timer = new CountDownTimer(updatedTime, 10) {

                // Callback fired on regular interval
                public void onTick(long millisUntilFinished) {
                    updatedTime = millisUntilFinished;

                    // Mise à jour
                    update();
                }

                // Callback fired when the time is up
                public void onFinish() {
                    updatedTime = 0;

                    // Mise à jour
                    update();
                    finish();

                }

            }.start();   // Start the countdown
        }

    }

    // Mettre en pause le compteur
    public void pause() {

        if (timer != null) {

            // Arreter le timer
            stop();

            // Mise à jour
            update();
        }
    }


    // Remettre à le compteur à la valeur initiale
    public void reset() {

        if (timer != null) {

            // Arreter le timer
            stop();
        }

        // Réinitialiser
        updatedTime = INITIAL_TIME;

        // Mise à jour
        update();

    }

    // Arrete l'objet CountDownTimer et l'efface
    public void stop() {
        timer.cancel();
        timer = null;
    }

    public int getMinutes() {
        return (int) (updatedTime / 1000)/60;
    }

    public int getSecondes() {
        int secs = (int) (updatedTime / 1000);
        return secs % 60;
    }

    public int getMillisecondes() {
        return (int) (updatedTime % 1000);
    }

    public String getNomCycle() {
        return nomCycle;
    }

    public String getNomSequence() {
        return nomSequence;
    }

    public String getNomTravail() {
        return nomTravail;
    }

    public void setNomSequence(String nomSequence) {
        this.nomSequence = nomSequence;
    }

    public void setNomCycle(String nomCycle) {
        this.nomCycle = nomCycle;
    }

    public void setNomTravail(String nomTravail) {
        this.nomTravail = nomTravail;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public Boolean getFin() {
        return fin;
    }

}

