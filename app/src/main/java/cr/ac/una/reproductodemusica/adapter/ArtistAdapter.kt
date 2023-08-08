package cr.ac.una.reproductodemusica.adapter

import android.graphics.BitmapFactory
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.una.reproductodemusica.R
import cr.ac.una.reproductodemusica.entity.Artist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class ArtistAdapter(var artistas : ArrayList<Artist>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_HEADER = 1
    private val VIEW_TYPE_ITEM = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return if (viewType == VIEW_TYPE_HEADER) {
            System.out.println("ERROR1")
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_header, parent, false)
            HeaderViewHolder(view)
        } else {
            System.out.println("ERROR2")
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.artist_item, parent, false)
            ViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = artistas[position]

        if (holder is ArtistAdapter.HeaderViewHolder) {
            holder.bind()
        } else if (holder is ArtistAdapter.ViewHolder) {

            holder.bind(item)
        }
    }

    fun updateData(newData: ArrayList<Artist>) {

        artistas = newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return artistas.size
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {

        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imagenView = itemView.findViewById<ImageView>(R.id.ImagenArtista)
        val nombreTextView = itemView.findViewById<TextView>(R.id.NombreArtista)
        val popularidadTextView = itemView.findViewById<TextView>(R.id.PopularidadArtista)


        fun bind(artista: Artist) {

            nombreTextView.text = artista.name
            popularidadTextView.text = artista.popularity.toString()


            GlobalScope.launch {
                withContext(Dispatchers.IO){
                    val url = URL(artista.images[0].url)
                    val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())

                    withContext(Dispatchers.Main){
                        imagenView.setImageBitmap(bmp)
                    }

                }
            }

        }
    }

}