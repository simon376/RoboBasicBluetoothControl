package de.othr.robobasic.robobasicbluetoothcontrol.viewmodels;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import de.othr.robobasic.robobasicbluetoothcontrol.DataRepository;
import de.othr.robobasic.robobasicbluetoothcontrol.data.Move;

public class MoveListViewModel extends AndroidViewModel {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<Move>> mObservableMoves;

    DataRepository mRepository;


    public MoveListViewModel(Application application) {
        super(application);

        mObservableMoves = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableMoves.setValue(null);

        mRepository = DataRepository.getInstance(application);

        LiveData<List<Move>> moves = mRepository.getMoves();

        // observe the changes of the products from the database and forward them
        mObservableMoves.addSource(moves, mObservableMoves::setValue);
    }

    /**
     * Expose the LiveData Moves query so the UI can observe it.
     */
    public LiveData<List<Move>> getMoves() {
        return mObservableMoves;
    }

    public void insert(Move move) { mRepository.insert(move); }

}
