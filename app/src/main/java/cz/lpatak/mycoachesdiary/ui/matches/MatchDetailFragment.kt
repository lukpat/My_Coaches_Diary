package cz.lpatak.mycoachesdiary.ui.matches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.databinding.FragmentMatchDetailBinding
import cz.lpatak.mycoachesdiary.ui.matches.util.TabsMatchManager

class MatchDetailFragment : Fragment() {
    private lateinit var binding: FragmentMatchDetailBinding
    private val args: MatchDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_match_detail, container, false)

        setupViewPager()

        return binding.root
    }

    private fun setupViewPager() {
        val adapter = TabsMatchManager(activity?.supportFragmentManager!!, lifecycle, args)
        binding.viewPager.adapter = adapter

        val names: Array<String> = arrayOf("Informace", "Statistiky")

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = names[position]
        }.attach()
    }


}





