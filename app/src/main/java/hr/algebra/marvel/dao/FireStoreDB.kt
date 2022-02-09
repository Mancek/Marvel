package hr.algebra.marvel.dao

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.database.Cursor
import android.database.MatrixCursor
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import hr.algebra.marvel.model.Character

var globalCursor : MatrixCursor? = MatrixCursor(arrayOf("apiId", "name", "description", "picturePath"))
class FireStoreDB : MarvelRepository {
    private val database = FirebaseFirestore.getInstance()

    override fun delete(selection: String?, selectionArgs: Array<String>?): Int {
        val userId = FirebaseAuth.getInstance().currentUser?.email
        database.collection(userId.toString()).document(selectionArgs!![0]).delete()
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully deleted!")
            }
            .addOnFailureListener {
                    e -> Log.w(TAG, "Error deleting document", e)
            }
        return 1
    }

    override fun update(
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val character = Character(
            values!!.getAsLong(Character::apiId.name),
            values.getAsString(Character::name.name),
            values.getAsString(Character::description.name),
            values.getAsString(Character::picturePath.name)
        )
        val userId = FirebaseAuth.getInstance().currentUser?.email
        database.collection(userId.toString()).document(character.apiId.toString()).set(character)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener {
                    e -> Log.w(TAG, "Error updating document", e)
            }
        return character.apiId.toInt()
    }

    override fun insert(values: ContentValues?): Long {
        val character = Character(
            values!!.getAsLong(Character::apiId.name),
            values.getAsString(Character::name.name),
            values.getAsString(Character::description.name),
            values.getAsString(Character::picturePath.name)
        )
        val userId = FirebaseAuth.getInstance().currentUser?.email
        database.collection(userId.toString()).document(character.apiId.toString()).set(character, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener {
                    e -> Log.w(TAG, "Error writing document", e)
            }
        return character.apiId
    }

    override fun query(
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor = globalCursor
        cursor!!.moveToFirst()
        return cursor
    }

}