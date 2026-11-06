package com.devtermo.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.devtermo.app.data.local.dao.PalavraDao
import com.devtermo.app.data.local.dao.PartidaDao
import com.devtermo.app.data.local.entity.PalavraEntity
import com.devtermo.app.data.local.entity.PartidaEntity

/** Responsavel: Integrante 4. Instancia unica (singleton). */
@Database(
    entities = [PalavraEntity::class, PartidaEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun palavraDao(): PalavraDao
    abstract fun partidaDao(): PartidaDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "devtermo.db"
                ).build().also { instance = it }
            }
    }
}
