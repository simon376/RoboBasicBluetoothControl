package de.othr.robobasic.robobasicbluetoothcontrol.data;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MoveDao {
    @Query("SELECT * FROM move")
    LiveData<List<Move>> getAll();    // wird verwendet in der MoveList-Activity

    @Query("SELECT * FROM move WHERE id IN (:moveIds)")
    List<Move> getAllByIds(int[] moveIds);

    @Query("SELECT * FROM move WHERE name LIKE :movename LIMIT 1")
    LiveData<Move> getByName(String movename);

    @Query("SELECT * FROM move WHERE id LIKE :id LIMIT 1")
    LiveData<Move> getById(int id);

    @Query("SELECT * FROM move WHERE message LIKE :msg LIMIT 1")
    Move getByMessage(String msg);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Move... moves);

    @Insert
    void insert(Move move);

    @Update
    public void update(Move... moves);

    @Delete
    void delete(Move... moves);

}
