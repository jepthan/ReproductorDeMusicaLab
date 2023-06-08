package cr.ac.una.reproductodemusica.entity

data class AlbumResponse(
    val name: String,
    val images: List<Image>,
    val id: String,
    val tracks: Tracks,
    val genres: List<String>
)