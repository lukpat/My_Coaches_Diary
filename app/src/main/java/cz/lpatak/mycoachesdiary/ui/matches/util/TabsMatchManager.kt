package cz.lpatak.mycoachesdiary.ui.matches.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import cz.lpatak.mycoachesdiary.ui.matches.MatchDetailFragmentArgs
import cz.lpatak.mycoachesdiary.ui.matches.MatchDetailFragmentInfo
import cz.lpatak.mycoachesdiary.ui.matches.MatchDetailFragmentStats

class TabsMatchManager(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    args: MatchDetailFragmentArgs
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments: ArrayList<Fragment> = arrayListOf(
        MatchDetailFragmentInfo(args.match),
        MatchDetailFragmentStats(args.match)
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}