package cz.lpatak.mycoachesdiary.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.databinding.FragmentMatchStatsBinding

class MatchStatsFragment : Fragment() {
    private lateinit var binding: FragmentMatchStatsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_match_stats, container, false)

        return binding.root
    }

}
