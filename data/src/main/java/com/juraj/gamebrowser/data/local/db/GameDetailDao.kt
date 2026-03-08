package com.juraj.gamebrowser.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.juraj.gamebrowser.data.local.entity.GameDetailEntity

@Dao
interface GameDetailDao {

    @Query("SELECT * FROM game_details WHERE id = :id")
    suspend fun getById(id: Int): GameDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: GameDetailEntity)
}
