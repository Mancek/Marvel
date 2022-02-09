package hr.algebra.marvel

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import hr.algebra.marvel.api.MarvelFetcher
import hr.algebra.marvel.databinding.ActivitySplashScreenBinding
import hr.algebra.marvel.framework.*


private const val DELAY = 3000L
const val DATA_IMPORTED = "hr.algebra.marvel.data_imported"
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAnimations()
        redirect()
    }

    private fun startAnimations() {
        binding.ivSplash.startAnimation(R.anim.rotate)
    }

    private fun redirect() {
        if (getBooleanPreference(DATA_IMPORTED)) {
            WorkManager.getInstance(this)
                .enqueueUniqueWork(
                    MarvelSelectorService.UNIQUE_WORK_NAME,
                    ExistingWorkPolicy.KEEP,
                    MarvelSelectorService.buildWorkRequest())
        } else {
            if (isOnline()) {
                Intent(this, MarvelService::class.java).apply {
                    MarvelService.enqueue(
                        this@SplashScreenActivity,
                        this
                    )
                }

            } else {
                Toast.makeText(this, getString(R.string.arent_connected), Toast.LENGTH_SHORT).show()
                callDelayed(DELAY) { finish() }
            }
        }
    }

}