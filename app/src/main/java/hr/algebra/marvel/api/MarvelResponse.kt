package hr.algebra.marvel.api

import com.google.gson.annotations.SerializedName

data class MarvelResponse(
    @SerializedName("data") val data: MarvelData,
    @SerializedName("code") val code: Int,
    @SerializedName("status") val status: String
)
