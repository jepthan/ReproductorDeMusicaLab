package cr.ac.una.reproductodemusica

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cr.ac.una.reproductodemusica.adapter.TracksAdapter
import cr.ac.una.reproductodemusica.databinding.FragmentAlbumBinding
import cr.ac.una.reproductodemusica.entity.Track
import cr.ac.una.reproductodemusica.view.AlbumViewModel
import cr.ac.una.reproductodemusica.view.TracksViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL


class AlbumFragment : Fragment() {

    private var _binding: FragmentAlbumBinding? = null
    private lateinit var viewModel: AlbumViewModel
    private lateinit var tracks: List<Track>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAlbumBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(AlbumViewModel::class.java)

        try {
            val albumId = requireArguments().getString("AlbumId")!!
            println("AlbumName: $albumId")

        } catch (e: Exception) {
            println(e.message)
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.updateAlbum(requireArguments().getString("AlbumId")!!)
        viewModel.albumLiveData.observe(viewLifecycleOwner) {

            binding.albumName.text = it.name
            binding.ArtistaName.text = it.artists[0].name

            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    val url = URL(it.images[0].url)
                    val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    withContext(Dispatchers.Main) {
                        binding.albumImage.setImageBitmap(bmp)
                    }
                }
            }

        }

        tracks = mutableListOf<Track>()
        var adapter = TracksAdapter(tracks as ArrayList<Track>)
        adapter.page = 2
        binding.listTracksAlbum.adapter = adapter

        binding.listTracksAlbum.layoutManager = LinearLayoutManager(requireContext())
        viewModel.listLiveData.observe(viewLifecycleOwner) { elements ->

            adapter.updateData(elements as ArrayList<Track>)
            tracks = elements

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}