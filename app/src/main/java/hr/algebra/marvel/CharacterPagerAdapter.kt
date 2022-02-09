package hr.algebra.marvel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.squareup.picasso.Picasso
import hr.algebra.marvel.model.Character
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class CharacterPagerAdapter(private val context: Context, private val items: MutableList<Character>)
    : RecyclerView.Adapter<CharacterPagerAdapter.ViewHolder>(){
    class ViewHolder(characterView: View) : RecyclerView.ViewHolder(characterView){

        private val ivItem = characterView.findViewById<ImageView>(R.id.ivItem)
        private val ivQrCode = characterView.findViewById<ImageView>(R.id.ivQrCode)
        private val ivRead = characterView.findViewById<ImageView>(R.id.ivRead)
        private val tvTitle = characterView.findViewById<TextView>(R.id.tvTitle)
        private val tvExplanation = characterView.findViewById<TextView>(R.id.tvExplanation)
        private val tvDate = characterView.findViewById<TextView>(R.id.tvDate)
        fun bind(character: Character) {
            Picasso.get()
                .load(File(character.picturePath))
                .error(R.drawable.noimage)
                .transform(RoundedCornersTransformation(50, 5))
                .into(ivItem)
            tvTitle.text = character.name
            tvExplanation.text = character.description
            tvDate.text = character.apiId.toString()
            ivRead.setImageResource(R.drawable.green_flag)
            ivQrCode.setImageBitmap(getQrCodeBitmap(character.toString()))
        }
        private fun getQrCodeBitmap(content: String): Bitmap {
            val size = 300
            val bits = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size)
            return Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
                for (x in 0 until size) {
                    for (y in 0 until size) {
                        it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
                    }
                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(characterView = LayoutInflater
        .from(parent.context).inflate(R.layout.character_pager, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount() = items.size
}