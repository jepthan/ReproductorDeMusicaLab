package cr.ac.una.reproductodemusica

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cr.ac.una.reproductodemusica.adapter.ArtistAdapter
import cr.ac.una.reproductodemusica.adapter.TracksAdapter
import cr.ac.una.reproductodemusica.databinding.FragmentArtistaRelBinding
import cr.ac.una.reproductodemusica.entity.Artist
import cr.ac.una.reproductodemusica.entity.Track
import cr.ac.una.reproductodemusica.view.ArtistViewModel


class ArtistaRelFragment : Fragment() {

    private var _binding: FragmentArtistaRelBinding? = null

    private lateinit var viewModel: ArtistViewModel
    private lateinit var Artistas: List<Artist>

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArtistaRelBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(ArtistViewModel::class.java)

        try {
            val artistId = requireArguments().getString("ArtistId")!!
            println("ArtistId: $artistId")


        } catch (e: Exception) {
            println(e.message)
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRelatedArtist(requireArguments().getString("ArtistId")!!)



        Artistas = mutableListOf<Artist>()
        var adapter =  ArtistAdapter(Artistas as ArrayList<Artist>)
        binding.listTopArtists.adapter = adapter
        binding.listTopArtists.layoutManager = LinearLayoutManager(requireContext())

        viewModel.artistRelLiveData.observe(viewLifecycleOwner) { elements ->
            System.out.println("ERROR3")
            adapter.updateData(elements as ArrayList<Artist>)
            Artistas = elements
        }
    }


}