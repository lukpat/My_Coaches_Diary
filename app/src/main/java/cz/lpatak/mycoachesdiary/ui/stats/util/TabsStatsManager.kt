package cz.lpatak.mycoachesdiary.ui.stats.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import cz.lpatak.mycoachesdiary.ui.stats.MatchStatsFragment
import cz.lpatak.mycoachesdiary.ui.stats.TrainingStatsFragment

class TabsStatsManager(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle
) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments: ArrayList<Fragment> = arrayListOf(
            MatchStatsFragment(),
            TrainingStatsFragment()
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}