package com.devtermo.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.devtermo.app.data.local.dao.PalavraDao
import com.devtermo.app.data.local.dao.PartidaDao
import com.devtermo.app.data.local.entity.PalavraEntity
import com.devtermo.app.data.local.entity.PartidaEntity

/** Responsavel: Integrante 4. Instancia unica (singleton). */
@Database(
    entities = [PalavraEntity::class, PartidaEntity::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun palavraDao(): PalavraDao
    abstract fun partidaDao(): PartidaDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        /** v1 -> v2: chave estrangeira partidas.palavraId -> palavras.id (ON DELETE SET NULL). */
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE partidas_nova (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        palavraId INTEGER,
                        jogador TEXT NOT NULL,
                        resultado TEXT NOT NULL,
                        tentativas INTEGER NOT NULL,
                        pontuacao INTEGER NOT NULL,
                        data INTEGER NOT NULL,
                        FOREIGN KEY(palavraId) REFERENCES palavras(id) ON UPDATE NO ACTION ON DELETE SET NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO partidas_nova (id, palavraId, jogador, resultado, tentativas, pontuacao, data)
                    SELECT id,
                           CASE WHEN palavraId IN (SELECT id FROM palavras) THEN palavraId ELSE NULL END,
                           jogador, resultado, tentativas, pontuacao, data
                    FROM partidas
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE partidas")
                db.execSQL("ALTER TABLE partidas_nova RENAME TO partidas")
                db.execSQL("CREATE INDEX index_partidas_palavraId ON partidas (palavraId)")
                db.execSQL("CREATE INDEX index_partidas_jogador ON partidas (jogador)")
            }
        }

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "devtermo.db"
                ).addMigrations(MIGRATION_1_2).build().also { instance = it }
            }
    }
}