package hr.algebra.marvel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.marvel.databinding.FragmentCharactersBinding
import hr.algebra.marvel.framework.fetchItems
import hr.algebra.marvel.model.Character

class CharactersFragment : Fragment() {

    private lateinit var binding: FragmentCharactersBinding
    private lateinit var characters: MutableList<Character>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharactersBinding.inflate(inflater, container, false)
        characters = requireContext().fetchItems()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CharacterAdapter(context, characters)
        }
    }

}