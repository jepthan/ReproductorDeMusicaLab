package cr.ac.una.reproductodemusica

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.una.reproductodemusica.adapter.TracksAdapter
import cr.ac.una.reproductodemusica.databinding.FragmentAlbumBinding
import cr.ac.una.reproductodemusica.databinding.FragmentArtistaBinding
import cr.ac.una.reproductodemusica.entity.Track
import cr.ac.una.reproductodemusica.view.AlbumViewModel
import cr.ac.una.reproductodemusica.view.ArtistViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import java.net.URL


class ArtistaFragment : Fragment() {

    private var _binding: FragmentArtistaBinding? = null

    private lateinit var viewModel: ArtistViewModel
    private lateinit var tracks: List<Track>

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArtistaBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(ArtistViewModel::class.java)

        try {
            val artistId = requireArguments().getString("ArtistId")!!
            println("ArtistId: $artistId")

            //binding.ArtistaName.text = artistId

        } catch (e: Exception) {
            println(e.message)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getArtis(requireArguments().getString("ArtistId")!!)
        viewModel.artistLiveData.observe(viewLifecycleOwner) {
            //binding.ArtistaName.text = it.tracks[0].artists[0].name
            binding.artistName.text = it.name

            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    val url = URL(it.images[0].url)
                    val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    withContext(Dispatchers.Main) {
                        binding.artistImage.setImageBitmap(bmp)
                    }
                }
            }

        }

        tracks = mutableListOf<Track>()
        var adapter =  TracksAdapter(tracks as ArrayList<Track>)
        binding.listTopArtists.adapter = adapter
        binding.listTopArtists.layoutManager = LinearLayoutManager(requireContext())

        viewModel.listLiveData.observe(viewLifecycleOwner) { elements ->
            adapter.updateData(elements as ArrayList<Track>)
            tracks = elements
        }
    }
}