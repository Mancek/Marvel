package hr.algebra.marvel.api

import com.google.gson.annotations.SerializedName

data class MarvelCharacter(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("modified") val modified : String,
    @SerializedName("description") val description : String,
    @SerializedName("thumbnail") val thumbnail: MarvelCharacterThumbnail
)

