package cr.ac.una.reproductodemusica.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cr.ac.una.reproductodemusica.converter.Converters
import cr.ac.una.reproductodemusica.dao.BusquedaDao
import cr.ac.una.reproductodemusica.entity.Busqueda

@Database(entities = [Busqueda::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun busquedaDao(): BusquedaDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "busqueda-database"
                    ).build()
                }
            }
            return instance!!
        }
    }
}