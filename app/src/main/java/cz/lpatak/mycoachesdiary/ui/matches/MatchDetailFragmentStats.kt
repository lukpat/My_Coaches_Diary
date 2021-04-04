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
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.StatsUIModel


class MatchDetailFragmentStats(
        private val matchFromArgs: Match,
        private val statsUIModel: StatsUIModel
) : Fragment() {
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
            statsModel = statsUIModel
        }

        setStats()

        return binding.root
    }


    private fun setStats() {
        statsUIModel.scoreTeam.value = matchFromArgs.scoreTeam.toString()
        statsUIModel.scoreOpponent.value = matchFromArgs.scoreOpponent.toString()
        statsUIModel.powerPlaysTeam.value = matchFromArgs.powerPlaysTeam.toString()
        statsUIModel.powerPlaysOpponent.value = matchFromArgs.powerPlaysOpponent.toString()
        statsUIModel.powerPlaysTeamSuccess.value = matchFromArgs.powerPlaysTeamSuccess.toString()
        statsUIModel.powerPlaysOpponentSuccess.value =
                matchFromArgs.powerPlaysOpponentSuccess.toString()
        statsUIModel.shotsTeam.value = matchFromArgs.shotsTeam.toString()
        statsUIModel.shotsOpponent.value = matchFromArgs.shotsOpponent.toString()
        statsUIModel.shotsToBlock.value = matchFromArgs.shotsToBlock.toString()
        statsUIModel.shotsOutside.value = matchFromArgs.shotsOutside.toString()
    }
}