package cz.lpatak.mycoachesdiary.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.firebase.Timestamp
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Match
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.databinding.FragmentMatchStatsBinding
import cz.lpatak.mycoachesdiary.ui.stats.viewmodel.MatchStatsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MatchStatsFragment : Fragment() {
    private lateinit var binding: FragmentMatchStatsBinding
    private val args: MatchStatsFragmentArgs by navArgs()
    private val matchStatsViewModel: MatchStatsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_match_stats, container, false)

        loadMatchStats(args.category, args.all, args.dateFrom, args.dateTo)

        return binding.root
    }

    private fun loadMatchStats(
        matchCategory: String,
        all: Boolean,
        dateFrom: Timestamp,
        dateTo: Timestamp
    ) {
        val matchList: MutableList<Match> = mutableListOf()

        matchStatsViewModel.loadMatchesFilter(matchCategory, all, dateFrom, dateTo)
            .observe(viewLifecycleOwner, { result ->
                if (result is Result.Success) {
                    matchList.addAll(result.data)
                    setUI(matchList)
                }
            })
    }

    private fun setUI(matchList: MutableList<Match>) {
        if (matchList.size == 0) {
            binding.moreThanZero = false
            binding.noMatches.text = getString(R.string.no_data_found)
        } else {
            binding.moreThanZero = true
            binding.goalsLayout.name.text = getString(R.string.goals)
            binding.powerPlaysLayout.name.text = getString(R.string.powerPlays)
            binding.powerPlaysSuccesLayout.name.text = getString(R.string.success)
            binding.shotsLayout2.name.text = getString(R.string.shots)
            binding.goalkeeperPercLayout.name.text = getString(R.string.goalkeeprPercentage)

            matchStatsViewModel.setUI(matchList, binding)
        }
    }
}
