package cr.ac.una.reproductodemusica.view

import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cr.ac.una.reproductodemusica.entity.*
import cr.ac.una.reproductodemusica.service.SpotifyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AlbumViewModel : ViewModel() {
    private val _ListMutableData: MutableLiveData<List<Track>> = MutableLiveData()
    val listLiveData : LiveData<List<Track>> =_ListMutableData
    private val _AlbumMutableData: MutableLiveData<AlbumResponse> = MutableLiveData()
    val albumLiveData : LiveData<AlbumResponse> =_AlbumMutableData


    private val spotifyServiceToken: SpotifyService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://accounts.spotify.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(SpotifyService::class.java)
    }
    private val spotifyService: SpotifyService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.spotify.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(SpotifyService::class.java)
    }

    private fun displayErrorMessage(errorMessage: String) {
        //Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        println("Error:")
        println(errorMessage)

    }

    private fun getAlbum(id: String){
        val clientId = "f13969da015a4f49bb1f1edef2185d4e"
        val clientSecret = "e3077426f4714315937111d5e82cd918"
        val base64Auth = Base64.encodeToString("$clientId:$clientSecret".toByteArray(), Base64.NO_WRAP)

        val tokenRequest = spotifyServiceToken.getAccessToken(
            "Basic $base64Auth",
            "client_credentials"
        )

        tokenRequest.enqueue(object : Callback<AccessTokenResponse> {
            override fun onResponse(call: Call<AccessTokenResponse>, response: Response<AccessTokenResponse>) {

                if (response.isSuccessful){

                    val accessTokenResponse = response.body()
                    val accessToken = accessTokenResponse?.accessToken

                    if (accessToken != null) {
                        val AlbumRequest = spotifyService.getAlbum("Bearer $accessToken", id)
                        AlbumRequest.enqueue(object : Callback<AlbumResponse> {
                            val list = mutableListOf<Track>()
                            override fun onResponse(call: Call<AlbumResponse>, response: Response<AlbumResponse>) {
                                if (response.isSuccessful) {
                                    val albumResponse = response.body()
                                    _AlbumMutableData.value = albumResponse!!
                                    println(albumResponse.toString())
                                    if (albumResponse != null && albumResponse.tracks.items.isNotEmpty()) {
                                        for (track in albumResponse!!.tracks.items){
                                            track.album = Album(albumResponse.name, albumResponse.images, albumResponse.id)
                                            list.add(track)
                                        }
                                        _ListMutableData.value = list
                                    }
                                    val tracks = albumResponse.tracks

                                } else {
                                    displayErrorMessage("Error en la respuesta del servidor.")
                                }
                            }
                            override fun onFailure(call: Call<AlbumResponse>, t: Throwable) {
                                displayErrorMessage("Error en la solicitud de tracks.")
                            }
                        })
                    } else {
                        displayErrorMessage("Error al obtener el accessToken.")
                    }
                    val trackRequest = spotifyService.getAlbum("Bearer $accessToken", id)
                }else{
                    System.out.println("Mensaje:    "+response.raw())
                    displayErrorMessage("Error en la respuesta del servidor.")

                }
            }
            override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                displayErrorMessage("Error en la solicitud de accessToken.")
            }
        })

    }




    public fun updateAlbum(id: String?) {
        if(id != null || id == "")
            getAlbum(id)

    }
}