package hr.algebra.marvel

import android.content.Context
import androidx.work.*
import hr.algebra.marvel.api.MarvelSelector


class MarvelSelectorService(val context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    companion object {

        const val UNIQUE_WORK_NAME = "hr.algebra.marvel.unique_work"

        fun buildWorkRequest(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<MarvelSelectorService>().build()
        }
    }

    override fun doWork(): Result {

        MarvelSelector(context).selectItems()

        return Result.success()
    }
}