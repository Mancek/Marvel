package hr.algebra.marvel.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.Instant

const val API_URL = "https://gateway.marvel.com:443/v1/public/"
const val PRIVATE_API_KEY = "key"
const val PUBLIC_API_KEY = "key"

interface MarvelApi {
    @GET("characters")
    fun fetchItems(@Query("hash") hash:String, @Query("ts") ts: String, @Query("apikey") apikey: String = PUBLIC_API_KEY, @Query("limit") limit: String = "10" ) : Call<MarvelResponse>
}
