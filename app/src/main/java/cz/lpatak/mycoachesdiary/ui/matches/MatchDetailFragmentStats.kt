package cz.lpatak.mycoachesdiary.ui.matches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Match
import cz.lpatak.mycoachesdiary.databinding.FragmentMatchDetailStatsBinding
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.MatchesViewModel
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.StatsUIModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MatchDetailFragmentStats(private val matchFromArgs: Match) : Fragment() {
    private val matchesViewModel: MatchesViewModel by viewModel()
    private lateinit var binding: FragmentMatchDetailStatsBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_match_detail_stats,
                container,
                false
        )
        with(binding) {
            lifecycleOwner = this@MatchDetailFragmentStats
            statsModel = StatsUIModel()
            btnSaveStats.setOnClickListener {
                updateTeam(statsModel)
            }
        }

        setStats(binding.statsModel)

        return binding.root
    }


    private fun setStats(uiModel: StatsUIModel?) {
        uiModel?.scoreTeam?.value = matchFromArgs.scoreTeam
        uiModel?.scoreOpponent?.value = matchFromArgs.scoreOpponent
        uiModel?.powerPlaysTeam?.value = matchFromArgs.powerPlaysTeam
        uiModel?.powerPlaysOpponent?.value = matchFromArgs.powerPlaysOpponent
        uiModel?.powerPlaysTeamSuccess?.value = matchFromArgs.powerPlaysTeamSuccess
        uiModel?.powerPlaysOpponentSuccess?.value = matchFromArgs.powerPlaysOpponentSuccess
        uiModel?.shotsTeam?.value = matchFromArgs.shotsTeam
        uiModel?.shotsOpponent?.value = matchFromArgs.shotsOpponent
        uiModel?.shotsToBlock?.value = matchFromArgs.shotsToBlock
        uiModel?.shotsOutside?.value = matchFromArgs.shotsOutside
    }


    private fun updateTeam(uiModel: StatsUIModel?) {
        if (uiModel != null) {
            matchesViewModel.updateMatch(
                    Match(
                            matchFromArgs.id,
                            matchFromArgs.team,
                            matchFromArgs.opponent,
                            matchFromArgs.date,
                            matchFromArgs.type,
                            matchFromArgs.playingTime,
                            matchFromArgs.note,
                            uiModel.scoreTeam.value!!,
                            uiModel.scoreOpponent.value!!,
                            uiModel.powerPlaysTeam.value!!,
                            uiModel.powerPlaysOpponent.value!!,
                            uiModel.powerPlaysTeamSuccess.value!!,
                            uiModel.powerPlaysOpponentSuccess.value!!,
                            uiModel.shotsTeam.value!!,
                            uiModel.shotsOpponent.value!!,
                            uiModel.shotsToBlock.value!!,
                            uiModel.shotsOutside.value!!
                    )
            )
        }
    }
}