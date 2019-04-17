package de.othr.robobasic.robobasicbluetoothcontrol.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MoveDao {
    @Query("SELECT * FROM move")
    List<Move> getAll();

    @Query("SELECT * FROM move WHERE id IN (:moveIds)")
    List<Move> loadAllByIds(int[] moveIds);

    @Query("SELECT * FROM move WHERE name LIKE :movename LIMIT 1")
    Move findByName(String movename);

    @Query("SELECT * FROM move WHERE id LIKE :id LIMIT 1")
    Move findById(String id);

    @Query("SELECT * FROM move WHERE message LIKE :msg LIMIT 1")
    Move findByMessage(String msg);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Move... moves);

    @Update
    public void update(Move... moves);

    @Delete
    void delete(Move... moves);

}
