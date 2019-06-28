package de.othr.robobasic.robobasicbluetoothcontrol.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.List;

/**
 * Repository handling the work with the movements database.
 * Implements Singleton Pattern
 */
public class DataRepository {

    private static DataRepository INSTANCE;

    private final AppDatabase mDatabase;
    private final MediatorLiveData<List<Move>> mObservableMoves;

    private final MoveDao mMoveDao;

    private DataRepository(Application application) {
        mDatabase = AppDatabase.getInstance(application);
        mMoveDao = mDatabase.moveDao();

        mObservableMoves = new MediatorLiveData<>();

        // MediatorLiveData to manually pre-populate the database

        mObservableMoves.addSource(mDatabase.moveDao().getAll(),
                moves -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableMoves.postValue(moves);
                    }
                });

    }

    /**
     * get Singleton Instance.
     *
     * @param application the application context
     * @return the Repository instance
     */
    public static DataRepository getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (DataRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DataRepository(application);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Get the list of moves from the database and get notified when the data changes.
     *
     * @return the moves
     */
    public LiveData<List<Move>> getMoves() {
        return mObservableMoves;
    }

    /**
     * get move live data.
     *
     * @param moveId the move id
     * @return the live data object
     */
    public LiveData<Move> loadMove(final int moveId) {
        return mMoveDao.getById(moveId);
    }

    /**
     * Insert move into database.
     *
     * @param move the move to be inserted
     */
    public void insert (Move move) {
        new insertAsyncTask(mMoveDao).execute(move);
    }

    /**
     * AsyncTask is used to insert Data into the Database asynchronously
     */
    private static class insertAsyncTask extends AsyncTask<Move, Void, Void> {
        private final MoveDao mAsyncTaskDao;

        /**
         * Instantiates a new Insert async task.
         *
         * @param dao the dao
         */
        insertAsyncTask(MoveDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Move... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
