package hr.algebra.marvel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager

class MarvelReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                MarvelSelectorService.UNIQUE_WORK_NAME,
                ExistingWorkPolicy.KEEP,
                MarvelSelectorService.buildWorkRequest())
    }
}