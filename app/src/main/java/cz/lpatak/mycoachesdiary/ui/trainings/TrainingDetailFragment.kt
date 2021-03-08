package cz.lpatak.mycoachesdiary.ui.trainings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.databinding.FragmentTrainingDetailBinding
import cz.lpatak.mycoachesdiary.ui.trainings.util.TabsTrainingManager

class TrainingDetailFragment : Fragment() {
    private lateinit var binding: FragmentTrainingDetailBinding
    private val args: TrainingDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_training_detail, container, false)

        setupViewPager()

        return binding.root
    }

    private fun setupViewPager() {
        val adapter = TabsTrainingManager(activity?.supportFragmentManager!!, lifecycle, args)
        binding.viewPager.adapter = adapter

        val names: Array<String> = arrayOf("Informace", "Cvičení")

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = names[position]
        }.attach()
    }


}





