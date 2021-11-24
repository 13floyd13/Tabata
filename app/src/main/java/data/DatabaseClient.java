package data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class DatabaseClient {

    // Instance unique permettant de faire le lien avec la base de données
    private static DatabaseClient instance;

    // Objet représentant la base de données de votre application
    private AppDatabase appDatabase;

    // Constructeur
    private DatabaseClient(final Context context) {

        // Créer l'objet représentant la base de données de votre application
        // à l'aide du "Room database builder"
        // MyToDos est le nom de la base de données
        //appDatabase = Room.databaseBuilder(context, AppDatabase.class, "MyToDos").build();

        // Ajout de la méthode addCallback permettant de populate (remplir) la base de données à sa création
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "MyToDos").addCallback(roomDatabaseCallback).build();
    }

    // Méthode statique
    // Retourne l'instance de l'objet DatabaseClient
    public static synchronized DatabaseClient getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseClient(context);
        }
        return instance;
    }

    // Retourne l'objet représentant la base de données de votre application
    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    // Objet permettant de populate (remplir) la base de données à sa création
    RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {

        // Called when the database is created for the first time.
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);


            db.execSQL("INSERT INTO travail (nom, temps, repos) VALUES(\"Pompes\", 20, 10);");
            db.execSQL("INSERT INTO travail (nom, temps, repos) VALUES(\"Abdos\", 20, 10);");
            db.execSQL("INSERT INTO travail (nom, temps, repos) VALUES(\"Squat\", 20, 10);");
            db.execSQL("INSERT INTO travail (nom, temps, repos) VALUES(\"Fentes\", 20, 10);");
            db.execSQL("INSERT INTO travail (nom, temps, repos) VALUES(\"Burpees\", 20, 10);");
            db.execSQL("INSERT INTO travail (nom, temps, repos) VALUES(\"Sauts\", 20, 10);");
            db.execSQL("INSERT INTO travail (nom, temps, repos) VALUES(\"Sprint\", 20, 10);");
            db.execSQL("INSERT INTO travail (nom, temps, repos) VALUES(\"Montée de Genoux\", 20, 10);");

            db.execSQL("INSERT INTO cycle (nom, repetition) VALUES(\"cycle pompes + squat\", 2);");
            db.execSQL("INSERT INTO CycleTravailCrossRef (cycleId, travailId) VALUES (1, 1)");
            db.execSQL("INSERT INTO CycleTravailCrossRef (cycleId, travailId) VALUES (1, 3)");

            db.execSQL("INSERT INTO cycle (nom, repetition) VALUES(\"cycle abdos + fentes\", 2);");
            db.execSQL("INSERT INTO CycleTravailCrossRef (cycleId, travailId) VALUES (2, 2)");
            db.execSQL("INSERT INTO CycleTravailCrossRef (cycleId, travailId) VALUES (2, 4)");

            db.execSQL("INSERT INTO cycle (nom, repetition) VALUES(\"cycle Burpees + Montée de Genoux\", 2);");
            db.execSQL("INSERT INTO CycleTravailCrossRef (cycleId, travailId) VALUES (3, 5)");
            db.execSQL("INSERT INTO CycleTravailCrossRef (cycleId, travailId) VALUES (3, 8)");

            db.execSQL("INSERT INTO cycle (nom, repetition) VALUES(\"cycle Saut + Sprint\", 2);");
            db.execSQL("INSERT INTO CycleTravailCrossRef (cycleId, travailId) VALUES (4, 6)");
            db.execSQL("INSERT INTO CycleTravailCrossRef (cycleId, travailId) VALUES (4, 7)");

            db.execSQL("INSERT INTO sequence (nom, repetition, tempsReposLong, description) VALUES(\"Musculation\", 2, 60,\"Sequence dédiée à la musculation\");");
            db.execSQL("INSERT INTO SequenceCycleCrossRef (sequenceId, cycleId) VALUES(1,1)");
            db.execSQL("INSERT INTO SequenceCycleCrossRef (sequenceId, cycleId) VALUES(1,2)");

            db.execSQL("INSERT INTO sequence (nom, repetition, tempsReposLong, description) VALUES(\"Cardio\", 2, 60,\"Sequence dédiée au cardio\");");
            db.execSQL("INSERT INTO SequenceCycleCrossRef (sequenceId, cycleId) VALUES(2,3)");
            db.execSQL("INSERT INTO SequenceCycleCrossRef (sequenceId, cycleId) VALUES(2,4)");

            db.execSQL("INSERT INTO entrainement (nom, tempsPreparation, description, tempsRepos) VALUES(\"Entrainement intégré\", 10, \"Contient deux séquences, une muscu et une cardio\", 60)");
            db.execSQL("INSERT INTO EntrainementSequenceCrossRef (entrainementId, sequenceId) VALUES(1,1)");
            db.execSQL("INSERT INTO EntrainementSequenceCrossRef (entrainementId, sequenceId) VALUES(1,2)");






        }
    };
}
