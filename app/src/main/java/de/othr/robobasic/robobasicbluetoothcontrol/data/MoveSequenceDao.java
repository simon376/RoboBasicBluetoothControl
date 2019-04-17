package de.othr.robobasic.robobasicbluetoothcontrol.data;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

public interface MoveSequenceDao {

    @Query("SELECT * FROM moveSequence")
    LiveData<List<MoveSequence>> getAll();    // wird verwendet in der MoveList-Activity (TODO)

    @Query("SELECT * FROM moveSequence WHERE id IN (:moveSequenceIds)")
    List<MoveSequence> getAllByIds(int[] moveSequenceIds);

    @Query("SELECT * FROM moveSequence WHERE name LIKE :moveSequenceName LIMIT 1")
    LiveData<MoveSequence> getByName(String moveSequenceName);

    @Query("SELECT * FROM moveSequence WHERE id LIKE :id LIMIT 1")
    LiveData<MoveSequence> getById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(MoveSequence... moveSequences);

    @Update
    public void update(MoveSequence... moveSequences);

    @Delete
    void delete(MoveSequence... moveSequences);

}
