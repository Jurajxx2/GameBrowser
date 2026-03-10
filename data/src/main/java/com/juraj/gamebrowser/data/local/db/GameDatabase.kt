package com.juraj.gamebrowser.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.juraj.gamebrowser.data.local.entity.GameDetailEntity

@Database(
    entities = [GameDetailEntity::class],
    version = 1,
    exportSchema = true
)
abstract class GameDatabase : RoomDatabase() {

    abstract fun gameDetailDao(): GameDetailDao
}
