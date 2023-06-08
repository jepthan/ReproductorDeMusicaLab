package cr.ac.una.reproductodemusica.view

import android.util.Base64
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cr.ac.una.reproductodemusica.entity.AccessTokenResponse
import cr.ac.una.reproductodemusica.entity.Track
import cr.ac.una.reproductodemusica.entity.TrackResponse
import cr.ac.una.reproductodemusica.service.SpotifyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TracksViewModel : ViewModel() {
    private val _ListMutableData: MutableLiveData<List<Track>> = MutableLiveData()
    val listLiveData : LiveData<List<Track>> =_ListMutableData

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

    private fun searchTracks(query: String) {

        val clientId = "f13969da015a4f49bb1f1edef2185d4e"
        val clientSecret = "e3077426f4714315937111d5e82cd918"
        val base64Auth = Base64.encodeToString("$clientId:$clientSecret".toByteArray(), Base64.NO_WRAP)

        val tokenRequest = spotifyServiceToken.getAccessToken(
            "Basic $base64Auth",
            "client_credentials"
        )


        tokenRequest.enqueue(object : Callback<AccessTokenResponse> {
            override fun onResponse(call: Call<AccessTokenResponse>, response: Response<AccessTokenResponse>) {
                val list = mutableListOf<Track>()
                if (response.isSuccessful) {
                    val accessTokenResponse = response.body()
                    val accessToken = accessTokenResponse?.accessToken

                    if (accessToken != null) {

                        val searchRequest = spotifyService.searchTrack("Bearer $accessToken", query)
                        searchRequest.enqueue(object : Callback<TrackResponse> {
                            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                                if (response.isSuccessful) {
                                    val trackResponse = response.body()

                                    if (trackResponse != null && trackResponse.tracks.items.isNotEmpty()) {
                                        for (track in trackResponse!!.tracks.items){
                                            System.out.println(track.name + track.album.name)
                                            System.out.println(track.album.artists[0].toString())
                                            list.add(track)
                                        }
                                        _ListMutableData.value = list
                                    } else {
                                        displayErrorMessage("No se encontraron canciones.")
                                    }
                                } else {
                                    System.out.println("Mensaje:    "+response.raw())
                                    displayErrorMessage("Error en la respuesta del servidor.")
                                }
                            }

                            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                                displayErrorMessage("Error en la solicitud de búsqueda.")
                            }
                        })
                    } else {
                        displayErrorMessage("Error al obtener el accessToken.")
                    }
                } else {
                    System.out.println("Mensaje:    "+response.raw())
                    displayErrorMessage("Error en la respuesta del servidor.")
                }

            }

            override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                displayErrorMessage(t.toString())
                displayErrorMessage("Error en la solicitud de accessToken.TrackViewModel")
            }
        })
    }

    public fun updatelist(search: String?) {
        if(search != null || search == "")
            searchTracks(search)

    }
}