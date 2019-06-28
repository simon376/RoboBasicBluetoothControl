package de.othr.robobasic.robobasicbluetoothcontrol.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * The interface Move Data Access Objects defines the SQL Querys used to access the Database.
 */
@SuppressWarnings("unused")
@Dao
public interface MoveDao {
    /**
     * Gets all moves in the database.
     *
     * @return the all
     */
    @Query("SELECT * FROM move")
    LiveData<List<Move>> getAll();

    /**
     * Gets a list of Moves by ids
     *
     * @param moveIds list of move ids
     * @return list of moves found in database
     */
    @Query("SELECT * FROM move WHERE id IN (:moveIds)")
    List<Move> getAllByIds(int[] moveIds);

    /**
     * Gets move by name.
     *
     * @param movename the move name
     * @return the move found in database
     */
    @Query("SELECT * FROM move WHERE name LIKE :movename LIMIT 1")
    LiveData<Move> getByName(String movename);

    /**
     * Gets move by id.
     *
     * @param id the id
     * @return the move found in database
     */
    @Query("SELECT * FROM move WHERE id LIKE :id LIMIT 1")
    LiveData<Move> getById(int id);

    /**
     * Gets move by message.
     *
     * @param msg the message
     * @return the  move found in database
     */
    @Query("SELECT * FROM move WHERE message LIKE :msg LIMIT 1")
    Move getByMessage(String msg);

    /**
     * Insert all moves into database.
     *
     * @param moves the moves
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Move... moves);

    /**
     * Insert single move into database.
     *
     * @param move the move
     */
    @Insert
    void insert(Move move);

    /**
     * Update moves.
     *
     * @param moves the moves
     */
    @Update
    void update(Move... moves);

    /**
     * Delete moves.
     *
     * @param moves the moves
     */
    @Delete
    void delete(Move... moves);

    /**
     * Delete all.
     */
    @Query("DELETE FROM move")
    void deleteAll();

}
