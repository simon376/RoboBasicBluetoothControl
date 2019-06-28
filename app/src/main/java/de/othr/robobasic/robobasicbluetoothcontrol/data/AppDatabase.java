package de.othr.robobasic.robobasicbluetoothcontrol.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;

/**
 * the Room App Database which stores all Moves (MoveSequences were removed again).
 * Implements Singleton Pattern
 */
@Database(entities = {Move.class}, version = 5)
public abstract class AppDatabase extends RoomDatabase {


    // Data Access Object
    public abstract MoveDao moveDao();

    @VisibleForTesting
    private static final String DATABASE_NAME = "basic-db";

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();


    private static volatile AppDatabase INSTANCE;

    /**
     * get AppDatabase Singleton Instance.
     * Database is created on first call of this function
     *
     * @param context the context
     * @return the database instance
     */
    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = buildDatabase(context.getApplicationContext());
                    INSTANCE.updateDatabaseCreated(context);
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
                .fallbackToDestructiveMigration()
                .addCallback(sRoomDatabaseCallback)
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


    /**
     * get a observable LiveData Object of the boolean set if the database is created.
     *
     * @return LiveData Object
     */
    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }


    private static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    /**
     * Populate the database in the background.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final MoveDao mDao;
        private final AppDatabase mDb;

        /**
         * Instantiates a new Populate db async.
         *
         * @param db the app database to populate
         */
        PopulateDbAsync(AppDatabase db) {
            mDb = db;
            mDao = mDb.moveDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            mDao.deleteAll();
            List<Move> moves = DataGenerator.generateMoves();
            mDao.insertAll(moves.toArray(new Move[0]));
            mDb.setDatabaseCreated();

            return null;
        }
    }
}
