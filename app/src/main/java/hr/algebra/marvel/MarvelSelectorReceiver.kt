package hr.algebra.marvel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.algebra.marvel.framework.startActivity

class MarvelSelectorReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.startActivity<HostActivity>()
    }
}