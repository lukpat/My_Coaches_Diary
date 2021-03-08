package cz.lpatak.mycoachesdiary.ui.matches

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.databinding.FragmentMatchesBinding
import cz.lpatak.mycoachesdiary.ui.matches.util.MatchesAdapter
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.MatchesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MatchesFragment : Fragment() {
    private val matchesViewModel: MatchesViewModel by viewModel()
    private lateinit var binding: FragmentMatchesBinding
    private val adapter: MatchesAdapter = MatchesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_matches, container, false)
        with(binding) {
            lifecycleOwner = this@MatchesFragment
            matchesList.adapter = adapter
            fabAddMatch.setOnClickListener {
                val directions =
                    MatchesFragmentDirections.actionNavigationMatchesToNavigationAddMatch()
                findNavController().navigate(directions)
            }
            isTeamSelected = matchesViewModel.isTeamSelected()
            filterOn = false
            btnSetFilter.setOnClickListener { applyFilter() }
        }

        adapter.setViewModel(matchesViewModel)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter -> {
                binding.filterOn = binding.filterOn == false
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        loadMatches()
    }

    private fun loadMatches() {
        matchesViewModel.loadMatches().observe(viewLifecycleOwner, { result ->
            binding.result = result
            if (result is Result.Success) {
                adapter.submitList(result.data)
            }
        })
    }

    private fun loadMatchesWithFilter(matchCategory: String) {
        matchesViewModel.loadMatchesFilter(matchCategory).observe(viewLifecycleOwner, { result ->
            binding.result = result
            if (result is Result.Success) {
                adapter.submitList(result.data)
            }
        })
    }

    private fun applyFilter() {
        val matchCategory = binding.matchCategory.selectedItem.toString()
        if (matchCategory == "Všechny zápasy") {
            loadMatches()
        } else {
            loadMatchesWithFilter(matchCategory)
        }
        binding.filterOn = false
    }

}