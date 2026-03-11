package com.juraj.gamebrowser.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.juraj.gamebrowser.data.local.entity.GameDetailEntity

@Database(
    entities = [GameDetailEntity::class],
    version = 2,
    exportSchema = true
)
abstract class GameDatabase : RoomDatabase() {

    abstract fun gameDetailDao(): GameDetailDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE game_details ADD COLUMN cachedAt INTEGER NOT NULL DEFAULT 0"
                )
            }
        }
    }
}
