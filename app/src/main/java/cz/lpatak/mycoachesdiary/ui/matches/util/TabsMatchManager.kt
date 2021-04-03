package cz.lpatak.mycoachesdiary.ui.matches.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import cz.lpatak.mycoachesdiary.databinding.FragmentMatchDetailInfoBinding
import cz.lpatak.mycoachesdiary.ui.matches.MatchDetailFragmentArgs
import cz.lpatak.mycoachesdiary.ui.matches.MatchDetailFragmentInfo
import cz.lpatak.mycoachesdiary.ui.matches.MatchDetailFragmentStats
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.MatchUIModel
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.StatsUIModel

class TabsMatchManager(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    args: MatchDetailFragmentArgs,
    UIModel: MatchUIModel,
    statsUIModel: StatsUIModel,
    binding: FragmentMatchDetailInfoBinding
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments: ArrayList<Fragment> = arrayListOf(
        MatchDetailFragmentInfo(args.match, UIModel, binding),
        MatchDetailFragmentStats(args.match, statsUIModel)
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}