package cr.ac.una.reproductodemusica

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cr.ac.una.reproductodemusica.databinding.FragmentMusicListBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MusicListFragment : Fragment() {

    private var _binding: FragmentMusicListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var searchtext: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMusicListBinding.inflate(inflater, container, false)

        try {
            searchtext = requireArguments().getString("edttext")!!
            println("strtext: AAAAAAAAAAAAAAAAAAAAa " + searchtext)
            //binding.textviewFirst.text = searchtext
        } catch (e: Exception) {
            println("strtext: AAAAAAAAAAAAAAAAAAAAa " + e)
        }


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding.textviewFirst
        /*
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_MusicListFragment_to_SecondFragment)
        }
        */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}