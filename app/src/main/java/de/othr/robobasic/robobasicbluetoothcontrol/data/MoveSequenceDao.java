package de.othr.robobasic.robobasicbluetoothcontrol.data;

import java.util.List;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

public interface MoveSequenceDao {

    @Query("SELECT * FROM moveSequence")
    List<MoveSequence> getAll();

    @Query("SELECT * FROM moveSequence WHERE id IN (:moveSequenceIds)")
    List<MoveSequence> loadAllByIds(int[] moveSequenceIds);

    @Query("SELECT * FROM moveSequence WHERE name LIKE :moveSequenceName LIMIT 1")
    MoveSequence findByName(String moveSequenceName);

    @Query("SELECT * FROM moveSequence WHERE id LIKE :id LIMIT 1")
    MoveSequence findById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(MoveSequence... moveSequences);

    @Update
    public void update(MoveSequence... moveSequences);

    @Delete
    void delete(MoveSequence... moveSequences);

}
