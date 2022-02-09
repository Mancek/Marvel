package hr.algebra.marvel

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import hr.algebra.marvel.api.MarvelFetcher

private const val JOB_ID = 1
class MarvelService : JobIntentService() {
    override fun onHandleWork(intent: Intent) {
        MarvelFetcher(this).fetchItems()
    }

    companion object {
        fun enqueue(context: Context, intent: Intent) {
            enqueueWork(context, MarvelService::class.java, JOB_ID, intent)
        }
    }
}