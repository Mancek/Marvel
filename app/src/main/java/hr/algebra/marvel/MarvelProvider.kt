package hr.algebra.marvel

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.google.firebase.provider.FirebaseInitProvider
import hr.algebra.marvel.dao.MarvelRepository
import hr.algebra.marvel.dao.getMarvelRepository
import hr.algebra.marvel.model.Character
import java.lang.IllegalArgumentException

private const val AUTHORITY = "hr.algebra.marvel.api.provider"
private const val PATH = "characters"
private const val ITEMS = 10
private const val ITEM_ID = 20

val MARVEL_PROVIDER_URI = Uri.parse("content://$AUTHORITY/$PATH")

private val URI_MATCHER = with(UriMatcher(UriMatcher.NO_MATCH)) {
    addURI(AUTHORITY, PATH, ITEMS)
    addURI(AUTHORITY, "$PATH/#", ITEM_ID)
    this
}

class MarvelProvider : ContentProvider() {

    private lateinit var marvelRepository: MarvelRepository

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when(URI_MATCHER.match(uri)) {
            ITEMS -> return marvelRepository.delete(selection, selectionArgs)
            ITEM_ID -> {
                uri.lastPathSegment?.let {
                    return marvelRepository.delete("${Character::apiId.name}=?", arrayOf(it))
                }
            }
        }
        throw IllegalArgumentException("Wrong uri")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val id = marvelRepository.insert(values)
        return ContentUris.withAppendedId(MARVEL_PROVIDER_URI, id)
    }

    override fun onCreate(): Boolean {
        marvelRepository = getMarvelRepository(context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? = marvelRepository.query(projection, selection, selectionArgs, sortOrder)

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        when(URI_MATCHER.match(uri)) {
            ITEMS -> return marvelRepository.update(values, selection, selectionArgs)
            ITEM_ID -> {
                uri.lastPathSegment?.let {
                    return marvelRepository.update(values, "${Character::apiId.name}=?", arrayOf(it))
                }
            }
        }
        throw IllegalArgumentException("Wrong uri")
    }
}