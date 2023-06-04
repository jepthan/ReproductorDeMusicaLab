package cr.ac.una.reproductodemusica

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.una.reproductodemusica.adapter.TracksAdapter
import cr.ac.una.reproductodemusica.databinding.FragmentMusicListBinding
import cr.ac.una.reproductodemusica.entity.Track
import cr.ac.una.reproductodemusica.view.TracksViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MusicListFragment : Fragment() {

    private var _binding: FragmentMusicListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var searchtext: String? = null
    private lateinit var viewModel: TracksViewModel
private lateinit var tracks: List<Track>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMusicListBinding.inflate(inflater, container, false)

        try {
            searchtext = requireArguments().getString("serchtext")!!

        } catch (e: Exception) {
            println(e.message)
        }

        viewModel = ViewModelProvider(this).get(TracksViewModel::class.java)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView = view.findViewById<RecyclerView>(R.id.list_tracks)
        tracks = mutableListOf<Track>()
        var adapter =  TracksAdapter(tracks as ArrayList<Track>)
        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(requireContext())


        viewModel.updatelist(searchtext)

        viewModel.listLiveData.observe(viewLifecycleOwner) { elements ->
            adapter.updateData(elements as ArrayList<Track>)
            tracks = elements
        }

        // Se llama el código del ViewModel que cargan los datos
        GlobalScope.launch(Dispatchers.Main) {
            viewModel.updatelist(searchtext)
        }
        

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}