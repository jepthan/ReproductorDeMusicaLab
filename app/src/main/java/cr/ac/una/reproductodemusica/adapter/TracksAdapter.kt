package cr.ac.una.reproductodemusica.adapter


import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import cr.ac.una.reproductodemusica.R
import cr.ac.una.reproductodemusica.entity.Album
import cr.ac.una.reproductodemusica.entity.Image
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

        fun bind(track: Track) {

            nombreTextView.text = track.name
            albumTextView.text = track.album.name

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