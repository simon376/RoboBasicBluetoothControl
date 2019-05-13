package de.othr.robobasic.robobasicbluetoothcontrol.data;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

/**
 * Repository handling the work with products and comments.
 */
public class DataRepository {

    private static DataRepository INSTANCE;

    private final AppDatabase mDatabase;
    private MediatorLiveData<List<Move>> mObservableMoves;
    private MediatorLiveData<List<MoveSequence>> mObservableMoveSequences;

    private MoveDao mMoveDao;
    private MoveSequenceDao mMoveSequenceDao;

    private DataRepository(Application application) {
        mDatabase = AppDatabase.getInstance(application);
        mMoveDao = mDatabase.moveDao();
//        mMoveSequenceDao = mDatabase.moveSequenceDao();

        mObservableMoves = new MediatorLiveData<>();
//        mObservableMoveSequences = new MediatorLiveData<>();

        // MediatorLiveData zum manuellen Vorbefüllen der Daten

        mObservableMoves.addSource(mDatabase.moveDao().getAll(),
                moves -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableMoves.postValue(moves);
                    }
                });

//        mObservableMoveSequences.addSource(mDatabase.moveSequenceDao().getAll(),
//                moveSequences -> {
//                    if(mDatabase.getDatabaseCreated().getValue() != null) {
//                        mObservableMoveSequences.postValue(moveSequences);
//                    }
//                });
    }

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
     * Get the list of products from the database and get notified when the data changes.
     */
    public LiveData<List<Move>> getMoves() {
        return mObservableMoves;
    }

    public LiveData<Move> loadMove(final int moveId) {
        return mMoveDao.getById(moveId);
    }

    public LiveData<List<MoveSequence>> getMoveSequences() {
        return mObservableMoveSequences;
    }

    public void insert (Move move) {
        new insertAsyncTask(mMoveDao).execute(move);
    }

    private static class insertAsyncTask extends AsyncTask<Move, Void, Void> {
        private MoveDao mAsyncTaskDao;

        insertAsyncTask(MoveDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Move... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void insert (MoveSequence moveSequence) {
        new insertSequenceAsyncTask(mMoveSequenceDao).execute(moveSequence);
    }

    private static class insertSequenceAsyncTask extends AsyncTask<MoveSequence, Void, Void> {
        private MoveSequenceDao mAsyncTaskDao;

        insertSequenceAsyncTask(MoveSequenceDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final MoveSequence... params) {
            mAsyncTaskDao.insertAll(params);
            return null;
        }
    }

}
