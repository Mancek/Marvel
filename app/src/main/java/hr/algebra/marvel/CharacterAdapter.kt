package hr.algebra.marvel

import android.app.AlertDialog
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.marvel.framework.startActivity
import hr.algebra.marvel.model.Character
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File


class CharacterAdapter(private val context: Context, private val characters: MutableList<Character>)
    : RecyclerView.Adapter<CharacterAdapter.ViewHolder>(){
    class ViewHolder(characterView: View) : RecyclerView.ViewHolder(characterView){

        private val ivItem = characterView.findViewById<ImageView>(R.id.ivItem)
        private val tvItem = characterView.findViewById<TextView>(R.id.tvItem)
        fun bind(character: Character) {
            Picasso.get()
                .load(File(character.picturePath))
                .error(R.drawable.noimage)
                .transform(RoundedCornersTransformation(50, 5))
                .into(ivItem)
            tvItem.text = character.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(characterView = LayoutInflater
        .from(parent.context).inflate(R.layout.character, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            context.startActivity<CharacterPagerActivity>(ITEM_POSITION, position)
        }
        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(context).apply {
                setTitle(R.string.delete)
                setMessage(context.getString(R.string.sure) + " '${characters[position].name}'?")
                setIcon(R.drawable.delete)
                setCancelable(true)
                setNegativeButton(R.string.cancel, null)
                setPositiveButton("OK") { _, _ -> deleteItem(position)}
                show()
            }
            true
        }
        holder.bind(characters[position])
    }

    private fun deleteItem(position: Int) {
        val character = characters[position]
        context.contentResolver.delete(
            ContentUris.withAppendedId(MARVEL_PROVIDER_URI, character.apiId!!),
            null,
            null)
        File(character.picturePath).delete()
        characters.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount() = characters.size
}