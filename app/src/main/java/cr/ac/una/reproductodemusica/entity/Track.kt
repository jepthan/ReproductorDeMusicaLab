package cr.ac.una.reproductodemusica.entity

data class Track(
    val name: String,
    var album: Album,
    val uri: String?
)