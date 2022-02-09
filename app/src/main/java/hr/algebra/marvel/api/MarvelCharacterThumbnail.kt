package hr.algebra.marvel.api

import com.google.gson.annotations.SerializedName

data class MarvelCharacterThumbnail(
    @SerializedName("path") val path : String,
    @SerializedName("extension") val extension : String,
)
