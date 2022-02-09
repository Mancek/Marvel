package hr.algebra.marvel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.marvel.databinding.ActivityItemPagerBinding
import hr.algebra.marvel.framework.fetchItems
import hr.algebra.marvel.model.Character

const val ITEM_POSITION = "hr.algebra.marvel.character_position"
class CharacterPagerActivity : AppCompatActivity() {

    private lateinit var characters: MutableList<Character>
    private lateinit var binding: ActivityItemPagerBinding

    private var itemPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPager()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun initPager() {
        characters = fetchItems()
        itemPosition = intent.getIntExtra(ITEM_POSITION, 0)
        binding.viewPager.adapter = CharacterPagerAdapter(this, characters)
        binding.viewPager.currentItem = itemPosition
    }
}