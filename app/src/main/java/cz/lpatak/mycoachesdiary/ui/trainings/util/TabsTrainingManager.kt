package cz.lpatak.mycoachesdiary.ui.trainings.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import cz.lpatak.mycoachesdiary.ui.trainings.TrainingDetailFragmentArgs
import cz.lpatak.mycoachesdiary.ui.trainings.TrainingDetailFragmentExercises
import cz.lpatak.mycoachesdiary.ui.trainings.TrainingDetailFragmentInfo
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingUIModel

class TabsTrainingManager(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        args: TrainingDetailFragmentArgs,
        UIModel: TrainingUIModel
) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments: ArrayList<Fragment> = arrayListOf(
            TrainingDetailFragmentInfo(args.training, UIModel),
            TrainingDetailFragmentExercises(args.training)
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}