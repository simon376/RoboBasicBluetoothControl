package de.othr.robobasic.robobasicbluetoothcontrol.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.List;

import de.othr.robobasic.robobasicbluetoothcontrol.data.DataRepository;
import de.othr.robobasic.robobasicbluetoothcontrol.data.Move;

/**
 * The MoveViewModel ist used to expose Database LiveData which can be observed in the Activities to build the UI.
 */
public class MoveViewModel extends AndroidViewModel {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<Move>> mObservableMoves;

    private final DataRepository mRepository;

    /**
     * Instantiates a new MoveViewModel.
     *
     * @param application the application
     */
    public MoveViewModel(Application application) {
        super(application);

        mObservableMoves = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableMoves.setValue(null);

        mRepository = DataRepository.getInstance(application);

        LiveData<List<Move>> moves = mRepository.getMoves();

        // observe the changes of the moves from the database and forward them
        mObservableMoves.addSource(moves, mObservableMoves::setValue);
    }

    /**
     * Expose the LiveData Moves query so the UI can observe it.
     *
     * @return the moves
     */
    public LiveData<List<Move>> getMoves() {
        return mObservableMoves;
    }

    /**
     * Insert move into Repository.
     *
     * @param move the move
     */
    public void insert(Move move) { mRepository.insert(move); }

    /**
     * Gets move from Repository.
     *
     * @param moveID the move id
     * @return the move live data object
     */
    public LiveData<Move> getMove(int moveID) { return mRepository.loadMove(moveID);}

}
