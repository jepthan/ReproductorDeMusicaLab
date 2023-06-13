package cr.ac.una.reproductodemusica

import android.app.SearchManager
import android.os.Bundle
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.widget.AutoCompleteTextView
import android.widget.CursorAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.WindowCompat
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import cr.ac.una.reproductodemusica.databinding.ActivityMainBinding
import cr.ac.una.reproductodemusica.db.AppDatabase
import cr.ac.una.reproductodemusica.entity.AccessTokenResponse
import cr.ac.una.reproductodemusica.entity.Busqueda
import cr.ac.una.reproductodemusica.entity.TrackResponse
import cr.ac.una.reproductodemusica.service.SpotifyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date
import android.provider.BaseColumns
import android.database.MatrixCursor
import android.database.Cursor
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        //WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)


    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)

        var menuItem = menu.findItem(R.id.action_search);

        var searchView = menuItem.actionView as SearchView;

        searchView.queryHint = "Buscar... ";
        //searchView.findViewById<AutoCompleteTextView>(R.id.).threshold = 1

        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = intArrayOf(R.id.item_label)
        val cursorAdapter = SimpleCursorAdapter(
            this,
            R.layout.search_item,
            null,
            from,
            to,
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )
        //val suggestions = listOf("Apple", "Blueberry", "Carrot", "Daikon")

        searchView.suggestionsAdapter = cursorAdapter


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                val bundle = Bundle()
                bundle.putString("serchtext", query)

                //searchTracks(query!!)

                val fragobj = MusicListFragment()
                fragobj.setArguments(bundle)
                val busquedaDao = AppDatabase.getInstance(applicationContext).busquedaDao()

                val busqueda = Busqueda(null, query!!, Date())
                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        busquedaDao.insert(busqueda)
                    }
                }



                findNavController(R.id.nav_host_fragment_content_main).navigate(
                    R.id.action_FirstFragment_self,
                    bundle
                )


                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {


                if (query!!.length >= 3) {

                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            val busquedaDao = AppDatabase.getInstance(applicationContext).busquedaDao()
                            val cursor =
                                MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))

                            val sugerencias = busquedaDao.buscarCoincidencias(query)

                            sugerencias.forEachIndexed { index, s ->
                                cursor.addRow(arrayOf(index, s))
                            }
                            withContext(Dispatchers.Main){
                                cursorAdapter.changeCursor(cursor)
                            }

                        }
                    }
                }

                return false
            }
        })
        searchView.setOnSuggestionListener(
            object : SearchView.OnSuggestionListener {
                override fun onSuggestionSelect(position: Int): Boolean {
                    return false
                }

                override fun onSuggestionClick(position: Int): Boolean {
                    //hideKeyboard()
                    val cursor = searchView.suggestionsAdapter.getItem(position) as Cursor
                    val selection =
                        cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                    searchView.setQuery(selection, false)

                    return true
                }

            })


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_search -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


}