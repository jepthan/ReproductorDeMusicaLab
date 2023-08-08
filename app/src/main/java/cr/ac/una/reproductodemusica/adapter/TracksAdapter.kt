package cr.ac.una.reproductodemusica.adapter



import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import cr.ac.una.reproductodemusica.AlbumFragment
import cr.ac.una.reproductodemusica.ArtistaFragment
import cr.ac.una.reproductodemusica.ArtistaRelFragment
import cr.ac.una.reproductodemusica.R
import cr.ac.una.reproductodemusica.entity.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL


class TracksAdapter(var tracks: ArrayList<Track>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_HEADER = 1
    private val VIEW_TYPE_ITEM = 0
    private var mediaPlayer = MediaPlayer()
    private var currentsource : String? = null
    private var lastview : View? = null

    public var page = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return if (viewType == VIEW_TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            ViewHolder(view)
        }

    }


    fun updateData(newData: ArrayList<Track>) {

        tracks = newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = tracks[position]

        if (holder is HeaderViewHolder) {
            holder.bind()
        } else if (holder is ViewHolder) {
            val trackitem = item
            holder.bind(trackitem)
        }
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {


        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imagenView = itemView.findViewById<ImageView>(R.id.ImagenTrack)
        val nombreTextView = itemView.findViewById<TextView>(R.id.NameTack)
        val albumTextView = itemView.findViewById<TextView>(R.id.AlbumTack)
        val menu = itemView.findViewById<ImageView>(R.id.more_actions)

        private fun manageItemClick(menuItem: MenuItem, track: Track): Boolean {
            return when(menuItem.itemId){
                R.id.AlbumInfo-> {
                    val bundle = Bundle()

                    bundle.putString("AlbumId", track.album.id)
                    println("AlbumId: ${track.album.id}")
                    var fragobj = AlbumFragment()
                    fragobj.arguments = bundle
                    if (page == 1){
                        itemView.findNavController().navigate(R.id.action_MusicListFragment_to_AlbumFragment, bundle)
                    }
                    else if(page == 2){
                        itemView.findNavController().navigate(R.id.action_AlbumFragment_self, bundle)
                    }else if (page == 3){
                        itemView.findNavController().navigate(R.id.action_ArtistFragment_to_albumFragment, bundle)
                    }else if (page == 4){
                        itemView.findNavController().navigate(R.id.action_ArtistFragment_to_ArtistaRelFragment, bundle)
                    }

                    true
                }
                R.id.ArtistInfo-> {
                    val bundle = Bundle()
                    bundle.putString("ArtistId", track.album.artists[0].id)
                    println("ArtistId: ${track.album.artists[0].id}")
                    var fragobj = ArtistaFragment()
                    fragobj.arguments = bundle
                    if (page == 1){
                        itemView.findNavController().navigate(R.id.action_MusicListFragment_to_ArtisFragment, bundle)
                    }
                    else if(page == 2){
                        itemView.findNavController().navigate(R.id.action_AlbumFragment_to_ArtistFragment, bundle)
                    }else if (page == 3){
                        itemView.findNavController().navigate(R.id.action_ArtistFragment_to_self, bundle)
                    }else if (page == 4){
                        itemView.findNavController().navigate(R.id.action_ArtistaRelFragment_to_ArtistFragment, bundle)
                    }
                    true
                }
                R.id.ArtistaRelacionados-> {
                    val bundle = Bundle()
                    bundle.putString("ArtistId", track.album.artists[0].id)
                    println("ArtistId: ${track.album.artists[0].id}")
                    var fragobj = ArtistaRelFragment()
                    fragobj.arguments = bundle
                    if (page == 1){
                        itemView.findNavController().navigate(R.id.action_MusicListFragment_to_ArtistaRelFragment, bundle)
                    }
                    else if(page == 2){
                        itemView.findNavController().navigate(R.id.action_AlbumFragment_to_ArtistaRelFragment, bundle)
                    }else if (page == 3){
                        itemView.findNavController().navigate(R.id.action_ArtistFragment_to_ArtistaRelFragment, bundle)
                    }else if (page == 4){
                        itemView.findNavController().navigate(R.id.action_ArtistaRelFragment_to_self, bundle)
                    }
                    true
                }
                else -> false
            }
        }


        fun bind(track: Track) {

            nombreTextView.text = track.name
            albumTextView.text = track.album.name
            menu.setOnClickListener {
                val Context = ContextThemeWrapper(itemView.context, R.style.PopupMenuStyle)
                val popupMenu = PopupMenu(Context, it)

                popupMenu.inflate(R.menu.menu_item)

                popupMenu.setOnMenuItemClickListener{ item: MenuItem? ->

                    manageItemClick(item!!, track)
                }
                popupMenu.show()
            }

            itemView.setOnClickListener{

                try {

                    if (mediaPlayer.isPlaying && currentsource == track.preview_url){
                        lastview!!.findViewById<ImageView>(R.id.ImageState).setImageResource(R.drawable.pause)
                        mediaPlayer.pause()
                    }else if (!mediaPlayer.isPlaying && currentsource == track.preview_url){
                        lastview!!.findViewById<ImageView>(R.id.ImageState).setImageResource(R.drawable.play)
                        mediaPlayer.start()
                    }else{
                        if (lastview != null){
                            lastview!!.findViewById<ImageView>(R.id.ImageState).setImageDrawable(null)
                            lastview!!.findViewById<ImageView>(R.id.ImageState).setBackgroundColor(Color.TRANSPARENT)
                        }
                        itemView.findViewById<ImageView>(R.id.ImageState).setImageResource(R.drawable.play)
                        itemView.findViewById<ImageView>(R.id.ImageState).setBackgroundColor(Color.BLACK)
                        lastview = itemView
                        mediaPlayer.reset()
                        mediaPlayer.setDataSource(track.preview_url)
                        currentsource = track.preview_url
                        mediaPlayer.prepare()
                        mediaPlayer.start()
                    }


                }
                catch (e: Exception){
                    Toast.makeText(itemView.context, "La cancion no cuenta con preview", Toast.LENGTH_SHORT).show()
                }

            }

            GlobalScope.launch {
                withContext(Dispatchers.IO){
                    val url = URL(track.album.images[0].url)
                    val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())

                    withContext(Dispatchers.Main){
                        imagenView.setImageBitmap(bmp)
                    }

                }
            }

        }
    }

}