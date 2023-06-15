package cr.ac.una.reproductodemusica.entity

import com.google.gson.annotations.SerializedName

data class AccessTokenResponse(
    @SerializedName("access_token")
    val accessToken: String?,
    @SerializedName("scope")
    val scope: String?
)