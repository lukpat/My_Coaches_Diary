package cz.lpatak.mycoachesdiary.ui.matches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.databinding.FragmentAddMatchBinding
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.MatchUIModel
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.MatchesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddMatchFragment : Fragment() {
    private val matchesViewModel: MatchesViewModel by viewModel()
    private lateinit var binding: FragmentAddMatchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_match, container, false)
        with(binding) {
            lifecycleOwner = this@AddMatchFragment
            matchModel = MatchUIModel()
            btnSaveMatch.setOnClickListener { createMatch() }
        }
        return binding.root
    }


    private fun createMatch() {
        matchesViewModel.addMatch(
            binding.matchModel!!.opponent.value.toString(),
            binding.matchModel!!.date.value.toString(),
            binding.type.selectedItem.toString(),
            binding.matchModel!!.playingTime.value!!
        )
        findNavController().navigateUp()
    }
}