package hr.algebra.marvel.framework

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.getSystemService
import hr.algebra.marvel.MARVEL_PROVIDER_URI
import hr.algebra.marvel.model.Character
import java.security.MessageDigest
import kotlin.text.Charsets.UTF_8

fun View.startAnimation(animationId: Int)
        = startAnimation(AnimationUtils.loadAnimation(context, animationId))

inline fun<reified T : Activity> Context.startActivity()
        = startActivity(Intent(this, T::class.java).apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
})

inline fun<reified T : Activity> Context.startActivity(key: String, value: Int)
        = startActivity(Intent(this, T::class.java).apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    putExtra(key, value)
})
inline fun<reified T: BroadcastReceiver> Context.sendBroadcast()
        = sendBroadcast(Intent(this, T::class.java))

fun md5(str: String): ByteArray
    = MessageDigest.getInstance("MD5").digest(str.toByteArray(UTF_8))
fun ByteArray.toHex()
    = joinToString(separator = "") { byte -> "%02x".format(byte) }

fun Context.setBooleanPreference(key: String, value: Boolean) =
    getSharedPreferences("hr.algebra.marvel_preferences", Context.MODE_PRIVATE)
        .edit()
        .putBoolean(key, value)
        .apply()


fun Context.getBooleanPreference(key: String) =
    getSharedPreferences("hr.algebra.marvel_preferences", Context.MODE_PRIVATE)
        .getBoolean(key, false)


fun Context.isOnline() : Boolean {
    val connectivityManager = getSystemService<ConnectivityManager>()
    connectivityManager?.activeNetwork?.let { network ->
        connectivityManager.getNetworkCapabilities(network)?.let { networkCapabilities ->
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
    }
    return false
}

fun callDelayed(delay: Long, function: Runnable) {
    Handler(Looper.getMainLooper()).postDelayed(
        function,
        delay
    )
}

fun Context.fetchItems() : MutableList<Character> {
    val characters = mutableListOf<Character>()
    val cursor = contentResolver?.query(
        MARVEL_PROVIDER_URI,
        null,
        null,
        null,
        null)
    while (cursor != null && cursor.moveToNext()) {
        characters.add(
            Character(
            cursor.getLong(cursor.getColumnIndexOrThrow(Character::apiId.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Character::name.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Character::description.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Character::picturePath.name))
            ))
    }
    return characters
}
