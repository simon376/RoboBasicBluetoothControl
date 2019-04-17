package de.othr.robobasic.robobasicbluetoothcontrol.data;

import android.content.Context;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

public abstract class AppDatabase extends RoomDatabase {
    // Data Access Objects
    public abstract MoveDao moveDao();
    public abstract MoveSequenceDao moveSequenceDao();

    @VisibleForTesting
    private static final String DATABASE_NAME = "basic-db";

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    //TODO: Add Migration Strategy

    private static volatile AppDatabase INSTANCE;

    static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = buildDatabase(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static AppDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries() //TODO remove, for development only
                .fallbackToDestructiveMigration()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);

                        AppDatabase database = AppDatabase.getInstance(context);

                        List<Move> moves = DataGenerator.generateMoves();
                        List<MoveSequence> moveSequences = DataGenerator.generateMoveSequences(moves);

                        insertData(database, moves, moveSequences);

                        //notify that the database was created and it's ready to be used
                        database.setDatabaseCreated();
                    }
                })
                .build();
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }

    private static void insertData(final AppDatabase database, final List<Move> moves,
                                   final List<MoveSequence> moveSequences) {
        database.runInTransaction(() -> {
            database.moveDao().insertAll(moves.toArray(new Move[0]));
            database.moveSequenceDao().insertAll(moveSequences.toArray(new MoveSequence[0]));
        });
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }
}
