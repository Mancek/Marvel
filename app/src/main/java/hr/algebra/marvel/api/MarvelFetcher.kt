package hr.algebra.marvel.api

import android.content.ContentValues
import android.content.Context
import android.util.Log
import hr.algebra.marvel.DATA_IMPORTED
import hr.algebra.marvel.MARVEL_PROVIDER_URI
import hr.algebra.marvel.MarvelReceiver
import hr.algebra.marvel.framework.md5
import hr.algebra.marvel.framework.sendBroadcast
import hr.algebra.marvel.framework.setBooleanPreference
import hr.algebra.marvel.framework.toHex
import hr.algebra.marvel.handler.downloadImageAndStore
import hr.algebra.marvel.model.Character
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Instant

class MarvelFetcher(private val context: Context) {
    private var marvelApi: MarvelApi
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        marvelApi = retrofit.create(MarvelApi::class.java)
    }

    fun fetchItems() {
        val time = Instant.now().epochSecond
        val request = marvelApi.fetchItems(
            md5("${time}${PRIVATE_API_KEY}${PUBLIC_API_KEY}").toHex(),
            time.toString()
        )

        request.enqueue(object: Callback<MarvelResponse> {
            override fun onResponse(
                call: Call<MarvelResponse>,
                response: Response<MarvelResponse>
            ) {
                response.body()?.let {
                    populateItems(it)
                }
            }

            override fun onFailure(call: Call<MarvelResponse>, t: Throwable) {
                Log.e(javaClass.name, t.message, t)
            }
        })
    }

    private fun populateItems(response: MarvelResponse) {
        val marvelCharacters = response.data.results
        GlobalScope.launch {
            marvelCharacters.forEach {
                val picturePath = downloadImageAndStore(
                    context,
                    it.thumbnail.path,
                    it.name.replace(" ", "_"),
                    it.thumbnail.extension
                )

                val values = ContentValues().apply {
                    put(Character::name.name, it.name)
                    put(Character::description.name, it.description)
                    put(Character::picturePath.name, picturePath ?: "")
                    put(Character::apiId.name, it.id)
                }
                context.contentResolver.insert(MARVEL_PROVIDER_URI, values)

            }
            context.setBooleanPreference(DATA_IMPORTED, true)
            context.sendBroadcast<MarvelReceiver>()
        }
    }


}