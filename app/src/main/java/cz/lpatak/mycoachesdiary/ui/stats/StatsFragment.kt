package cz.lpatak.mycoachesdiary.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.databinding.FragmentStatsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatsFragment : Fragment() {
    private val statsViewModel: StatsViewModel by viewModel()
    private lateinit var binding: FragmentStatsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stats, container, false)
        binding.isTeamSelected = statsViewModel.isTeamSelected()

        return binding.root
    }

}