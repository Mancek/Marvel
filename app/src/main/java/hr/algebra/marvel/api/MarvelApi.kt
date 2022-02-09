package hr.algebra.marvel.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.Instant

const val API_URL = "https://gateway.marvel.com:443/v1/public/"
const val PRIVATE_API_KEY = "fd03cf1cd381064e3b2e98056dabeb29d867e994"
const val PUBLIC_API_KEY = "d43e2bafa38eb515ae349200b40f5c25"

interface MarvelApi {
    @GET("characters")
    fun fetchItems(@Query("hash") hash:String, @Query("ts") ts: String, @Query("apikey") apikey: String = PUBLIC_API_KEY, @Query("limit") limit: String = "10" ) : Call<MarvelResponse>
}