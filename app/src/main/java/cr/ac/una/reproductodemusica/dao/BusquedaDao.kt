package cr.ac.una.reproductodemusica.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cr.ac.una.reproductodemusica.entity.Busqueda

@Dao
interface BusquedaDao {

    @Insert
    fun insert(entity: Busqueda)

    @Query("SELECT DISTINCT texto FROM busqueda WHERE texto LIKE '%' || :searchString || '%'" )
    fun buscarCoincidencias(searchString: String): List<String>


}