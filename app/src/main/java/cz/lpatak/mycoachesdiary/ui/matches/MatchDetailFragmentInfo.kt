package cz.lpatak.mycoachesdiary.ui.matches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Match
import cz.lpatak.mycoachesdiary.databinding.FragmentMatchDetailInfoBinding
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.MatchUIModel
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.MatchesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MatchDetailFragmentInfo(private val matchFromArgs: Match) : Fragment() {
    private val matchesViewModel: MatchesViewModel by viewModel()
    private lateinit var binding: FragmentMatchDetailInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_match_detail_info,
            container,
            false
        )
        with(binding) {
            lifecycleOwner = this@MatchDetailFragmentInfo
            matchModel = MatchUIModel()
            btnSaveMatch.setOnClickListener {
                updateTeam(matchModel)
            }
        }

        setMatch(binding.matchModel)

        return binding.root
    }

    private fun setMatch(uiModel: MatchUIModel?) {
        uiModel?.opponent?.value = matchFromArgs.opponent
        binding.type.setSelection(getIndex(matchFromArgs.type.toString()))
        uiModel?.date?.value = matchFromArgs.date
        uiModel?.playingTime?.value = matchFromArgs.playingTime
        uiModel?.note?.value = matchFromArgs.note
    }

    private fun getIndex(type: String): Int {
        return when (type) {
            "Přátelák" -> 0
            "Liga" -> 1
            "Turnaj" -> 2
            else -> 0
        }
    }

    private fun updateTeam(uiModel: MatchUIModel?) {
        if (uiModel != null) {
            matchesViewModel.updateMatch(
                Match(
                    matchFromArgs.id,
                    matchFromArgs.team,
                    uiModel.opponent.value.toString(),
                    uiModel.date.value.toString(),
                    binding.type.selectedItem.toString(),
                    uiModel.playingTime.value!!,
                    uiModel.note.value.toString(),
                    matchFromArgs.scoreTeam,
                    matchFromArgs.scoreOpponent,
                    matchFromArgs.powerPlaysTeam,
                    matchFromArgs.powerPlaysOpponent,
                    matchFromArgs.powerPlaysTeamSuccess,
                    matchFromArgs.powerPlaysOpponentSuccess,
                    matchFromArgs.shotsTeam,
                    matchFromArgs.shotsOpponent,
                    matchFromArgs.shotsToBlock,
                    matchFromArgs.shotsOutside
                )
            )
        }
    }
}