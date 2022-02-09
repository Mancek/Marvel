package hr.algebra.marvel.api

import android.content.ContentValues
import android.content.Context
import android.database.MatrixCursor
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hr.algebra.marvel.MarvelSelectorReceiver
import hr.algebra.marvel.dao.globalCursor
import hr.algebra.marvel.framework.sendBroadcast

import hr.algebra.marvel.model.Character


class MarvelSelector(private val context: Context) {

    fun selectItems() {
        globalCursor?.close()
        globalCursor = MatrixCursor(arrayOf("apiId", "name", "description", "picturePath"))
        val userId = FirebaseAuth.getInstance().currentUser?.email
        val task = FirebaseFirestore.getInstance().collection(userId.toString()).get()
            .addOnSuccessListener {
                for (document in it) {
                    val character = document.data
                    globalCursor!!.newRow()
                        .add("apiId", character[Character::apiId.name])
                        .add("name", character[Character::name.name])
                        .add("description", character[Character::description.name])
                        .add("picturePath", character[Character::picturePath.name]);
                }
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error ", e)
            }
        Tasks.await(task)
        context.sendBroadcast<MarvelSelectorReceiver>()
    }
}