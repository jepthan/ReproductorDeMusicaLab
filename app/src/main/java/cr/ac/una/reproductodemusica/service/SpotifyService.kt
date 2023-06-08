package cr.ac.una.reproductodemusica.service

import cr.ac.una.reproductodemusica.entity.*
import retrofit2.Call
import retrofit2.http.*

interface SpotifyService {
    @FormUrlEncoded
    @POST("api/token")
    fun getAccessToken(
        @Header("Authorization") authorization: String,
        @Field("grant_type") grantType: String
    ): Call<AccessTokenResponse>

    @GET("v1/search?type=track")
    fun searchTrack(
        @Header("Authorization") authorization: String,
        @Query("q") query: String
    ): Call<TrackResponse>

    @GET("v1/albums/{id}")
    fun getAlbum(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Call<AlbumResponse>

    @GET("v1/artists/{id}/top-tracks?market=ES")
    fun getArtisTop(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Call<ArtistResponse>

    @GET("v1/artists/{id}")
    fun getArtist(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Call<Artist>
}