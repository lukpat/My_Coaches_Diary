package cz.lpatak.mycoachesdiary.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.databinding.FragmentStatsBinding
import cz.lpatak.mycoachesdiary.ui.stats.util.TabsStatsManager
import cz.lpatak.mycoachesdiary.ui.stats.viewmodel.MatchStatsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatsFragment : Fragment() {
    private val matchStatsViewModel: MatchStatsViewModel by viewModel()
    private lateinit var binding: FragmentStatsBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stats, container, false)
        binding.isTeamSelected = matchStatsViewModel.isTeamSelected()
        setupViewPager()

        return binding.root
    }

    private fun setupViewPager() {
        val adapter = TabsStatsManager(activity?.supportFragmentManager!!, lifecycle)
        binding.viewPager.adapter = adapter

        val names: Array<String> = arrayOf("Tréninky", "Zápasy")

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = names[position]
        }.attach()
    }

}
