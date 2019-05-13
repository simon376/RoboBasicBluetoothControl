package de.othr.robobasic.robobasicbluetoothcontrol.viewmodels;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import de.othr.robobasic.robobasicbluetoothcontrol.data.DataRepository;
import de.othr.robobasic.robobasicbluetoothcontrol.data.Move;
import de.othr.robobasic.robobasicbluetoothcontrol.data.MoveSequence;

public class MyViewModel extends AndroidViewModel {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<Move>> mObservableMoves;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
 //   private final MediatorLiveData<List<MoveSequence>> mObservableMoveSequences;

    DataRepository mRepository;


    public MyViewModel(Application application) {
        super(application);

        mObservableMoves = new MediatorLiveData<>();
    //    mObservableMoveSequences = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableMoves.setValue(null);
     //   mObservableMoveSequences.setValue(null);

        mRepository = DataRepository.getInstance(application);

        LiveData<List<Move>> moves = mRepository.getMoves();
        LiveData<List<MoveSequence>> moveSequences = mRepository.getMoveSequences();

        // observe the changes of the moves from the database and forward them
        mObservableMoves.addSource(moves, mObservableMoves::setValue);
    //    mObservableMoveSequences.addSource(moveSequences, mObservableMoveSequences::setValue);
    }

    /**
     * Expose the LiveData Moves query so the UI can observe it.
     */
    public LiveData<List<Move>> getMoves() {
        return mObservableMoves;
    }

    public void insert(Move move) { mRepository.insert(move); }

    public LiveData<Move> getMove(int moveID) { return mRepository.loadMove(moveID);}

 /*   @Deprecated
    public LiveData<List<MoveSequence>> getMoveSequences() {
        return mObservableMoveSequences;
    }

    @Deprecated
    public void insert(MoveSequence moveSequence) { mRepository.insert(moveSequence); }
*/
}
